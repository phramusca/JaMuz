/*
 * Copyright (C) 2026 phramusca <phramusca@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package test.helpers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FixtureYamlTest {

	@Test
	void loadsEveryScenarioFixtureFromItsFunctionalTestDirectory() throws Exception {
		Path root = Path.of(TestSettings.getResourcesPath(), "albumFiles_yaml");
		assertEquals(24, validateSuite(root, "CheckNTest"));
		assertEquals(80, validateSuite(root, "MergeNTest"));
		assertEquals(152, validateSuite(root, "MergeCheckNTest"));
		assertEquals(9, validateSuite(root, "Merge1Test"));
		assertEquals(8, validateSuite(root, "REFERENCE"));
	}

	@Test
	void loadsTemplateWorksheetsAsRawCells() throws IOException {
		Path template = Path.of(TestSettings.getResourcesPath(), "albumFiles_yaml", "TEMPLATE");
		List<List<String>> mod = FixtureYaml.readTemplateRows(template.resolve("mod.yaml"), "mod");
		List<List<String>> secondSheet = FixtureYaml.readTemplateRows(
				template.resolve("Feuille2.yaml"), "Feuille2");

		assertTrue(mod.size() > 1);
		assertTrue(mod.stream().anyMatch(row -> row.contains("track")));
		assertTrue(secondSheet.stream().anyMatch(row -> row.contains("relativePath")));
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
				FixtureYaml.Fixture parsed = FixtureYaml.readFixture(fixture, releaseId, scenario);
				assertFalse(parsed.tracks().isEmpty(), fixture.toString());
				assertEquals(19, parsed.expectedResults().size(), fixture.toString());
				assertEquals(4, parsed.process().size(), fixture.toString());
				Album album = AlbumBuffer.getAlbum(releaseId, scenario);
				assertEquals(parsed.tracks().size(), album.getNbTracks(), fixture.toString());
			}
			return fixtures.size();
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
