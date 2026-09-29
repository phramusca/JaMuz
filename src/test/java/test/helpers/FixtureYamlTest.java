/*
 * Copyright (C) 2026 phramusca <phramusca@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package test.helpers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FixtureYamlTest {

	private static final List<String> CHECK_SCAN_ORDER = List.of(
			"8cfbb741-bd63-449f-9e48-4d234264c8d5",
			"7598d527-bc8d-4282-a72c-874f335d05ac",
			"6cc35892-c44f-4aa7-bfee-5f63eca70821",
			"c212b71b-848c-491c-8ae7-b62a993ae194",
			"13ca98f6-1a9f-4d76-a3b3-a72a16d91916",
			"be04bc1f-fc63-48f5-b1ca-2723f17d241d",
			"9dc7fe6a-3fa4-4461-8975-ecb7218b39a3",
			"9e097b10-8160-491e-a310-e26e54a86a10");

	@Test
	void loadsEveryScenarioFixtureFromItsFunctionalTestDirectory() throws Exception {
		Path root = Path.of(TestSettings.getResourcesPath(), "albumFiles_yaml");
		assertEquals(24, validateSuite(root, "CheckNTest"));
		assertEquals(80, validateSuite(root, "MergeNTest"));
		assertEquals(152, validateSuite(root, "MergeCheckNTest"));
		assertEquals(9, validateSuite(root, "Merge1Test"));
		assertEquals(8, validateSuite(root, "REFERENCE"));
	}

	private int validateSuite(Path root, String suite) throws Exception {
		Path suiteRoot = root.resolve(suite);
		AlbumBuffer.setTestSuite(suite);
		try (Stream<Path> paths = Files.walk(suiteRoot)) {
			List<Path> fixtures = paths.filter(path -> path.toString().endsWith(".yaml"))
					.sorted()
					.toList();
			for (Path fixture : fixtures) {
				String scenario = fixture.getParent().getFileName().toString();
				String releaseId = fixture.getFileName().toString().replaceFirst("\\.yaml$", "");
				assertTrue(scenarioBelongsToSuite(suite, scenario), fixture.toString());
				List<String> lines = Files.readAllLines(fixture);
				assertTrue(lines.indexOf("process:") < lines.indexOf("expectedResults:")
						&& lines.indexOf("expectedResults:") < lines.indexOf("tracks:"),
						fixture + " should list fixed-size sections before tracks");
				for (String section : List.of("process:", "expectedResults:", "tracks:")) {
					int sectionIndex = lines.indexOf(section);
					int precedingLine = sectionIndex - 1;
					if (section.equals("process:")) {
						assertTrue(precedingLine >= 0 && lines.get(precedingLine).isEmpty(),
								fixture + " should separate metadata from process");
					} else {
						assertTrue(lines.get(precedingLine).isEmpty(),
								fixture + " should separate top-level sections");
					}
				}
				FixtureYaml.Fixture parsed = FixtureYaml.readFixture(fixture, releaseId, scenario);
				assertFalse(parsed.tracks().isEmpty(), fixture.toString());
				assertEquals(19, parsed.expectedResults().size(), fixture.toString());
				assertEquals(4, parsed.process().size(), fixture.toString());
				validateRememberedCheckData(suite, scenario, releaseId, parsed, fixture);
				Album album = AlbumBuffer.getAlbum(releaseId, scenario);
				assertEquals(parsed.tracks().size(), album.getNbTracks(), fixture.toString());
			}
			return fixtures.size();
		}
	}

	private void validateRememberedCheckData(String suite, String scenario, String releaseId,
			FixtureYaml.Fixture fixture, Path path) {
		if (!suite.equals("CheckNTest")) {
			return;
		}

		int scanIndex = CHECK_SCAN_ORDER.indexOf(releaseId);
		assertTrue(scanIndex >= 0, path.toString());
		if (scenario.equals("CheckTest1_KO") || scenario.equals("CheckTest2_OK")) {
			assertEquals(Integer.toString(scanIndex), fixture.process().get("index"), path.toString());
			assertEquals("location.add", fixture.process().get("location"), path.toString());
		}

		if (scenario.equals("CheckTest2_OK") || scenario.equals("CheckTest3_DbOk")) {
			assertEquals("ok", fixture.expectedResults().get("cover").status(), path.toString());
			assertEquals("ok", fixture.expectedResults().get("genre").status(), path.toString());
			assertEquals("Reggae", fixture.expectedResults().get("genre").value(), path.toString());
			assertTrue(fixture.tracks().stream().allMatch(track ->
					"Reggae".equals(track.get("genre")) && "1".equals(track.get("nbCovers"))),
					path.toString());
		}

		if (scenario.equals("CheckTest3_DbOk")) {
			assertEquals(Integer.toString(scanIndex + 1), fixture.process().get("idPath"), path.toString());
			assertEquals("location.library", fixture.process().get("location"), path.toString());
		}
	}

	private boolean scenarioBelongsToSuite(String suite, String scenario) {
		switch (suite) {
			case "CheckNTest":
				return scenario.startsWith("CheckTest");
			case "MergeNTest":
				return scenario.startsWith("MergeDevice")
						&& !scenario.equals("MergeDevice5_3")
						&& !scenario.matches("MergeDevice(?:[789]_.*|10_.*|11_.*)");
			case "MergeCheckNTest":
				return scenario.startsWith("MergeDevice");
			case "Merge1Test":
				return scenario.startsWith("MergeTest");
			case "REFERENCE":
				return scenario.equals("MusicBrainz_REFERENCE_DO_NOT_MODIFY");
			default:
				return false;
		}
	}
}
