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
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.error.YAMLException;

final class FixtureYaml {

	private FixtureYaml() {
	}

	static Fixture readFixture(Path file, String releaseId, String scenario) throws IOException {
		Map<?, ?> document = readDocument(file);
		validateIdentity(document, file, 2, releaseId, scenario);

		List<Map<String, Object>> tracks = readMappingList(document.get("tracks"), "tracks", file);
		Map<String, ExpectedResult> expectedResults = new LinkedHashMap<>();
		Map<?, ?> rawResults = readMapping(document.get("expectedResults"), "expectedResults", file);
		for (Map.Entry<?, ?> entry : rawResults.entrySet()) {
			if (!(entry.getKey() instanceof String key)) {
				throw new IOException("Expected result key is not a string in " + file);
			}
			Map<?, ?> result = readMapping(entry.getValue(), "expected result '" + key + "'", file);
			String status = readString(result.get("status"), "status", file);
			if (!List.of("ok", "ko", "warning").contains(status)) {
				throw new IOException("Unknown result state '" + status + "' for '" + key + "' in " + file);
			}
			expectedResults.put(key, new ExpectedResult(status,
					readString(result.get("value"), "value for expected result '" + key + "'", file)));
		}

		Map<String, String> process = new LinkedHashMap<>();
		Map<?, ?> rawProcess = readMapping(document.get("process"), "process", file);
		for (Map.Entry<?, ?> entry : rawProcess.entrySet()) {
			if (!(entry.getKey() instanceof String key)) {
				throw new IOException("Process key is not a string in " + file);
			}
			process.put(key, readString(entry.getValue(), "process value '" + key + "'", file));
		}

		for (Map<String, Object> track : tracks) {
			for (Map.Entry<String, Object> entry : track.entrySet()) {
				if (entry.getKey().equals("ignore")) {
					if (!(entry.getValue() instanceof Boolean)) {
						throw new IOException("Track ignore flag must be a boolean in " + file);
					}
				} else if (!(entry.getValue() instanceof String)) {
					throw new IOException("Track field '" + entry.getKey() + "' must be a string in " + file);
				}
			}
		}

		return new Fixture(List.copyOf(tracks), Map.copyOf(expectedResults), Map.copyOf(process));
	}

	private static Map<?, ?> readDocument(Path file) throws IOException {
		LoaderOptions options = new LoaderOptions();
		options.setAllowDuplicateKeys(false);
		try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
			return readMapping(new Yaml(new SafeConstructor(options)).load(reader), "document", file);
		} catch (YAMLException ex) {
			throw new IOException("Invalid YAML test fixture: " + file, ex);
		}
	}

	private static void validateIdentity(Map<?, ?> document, Path file, int formatVersion,
			String releaseId, String scenario) throws IOException {
		if (!Integer.valueOf(formatVersion).equals(document.get("formatVersion"))) {
			throw new IOException("Unsupported test fixture format in " + file);
		}
		if (!releaseId.equals(document.get("releaseId")) || !scenario.equals(document.get("scenario"))) {
			throw new IOException("Fixture identity does not match its path: " + file);
		}
	}

	private static List<Map<String, Object>> readMappingList(Object value, String name, Path file)
			throws IOException {
		List<?> list = readList(value, name, file);
		List<Map<String, Object>> mappings = new ArrayList<>();
		for (Object item : list) {
			Map<?, ?> source = readMapping(item, name + " item", file);
			Map<String, Object> mapping = new LinkedHashMap<>();
			for (Map.Entry<?, ?> entry : source.entrySet()) {
				if (!(entry.getKey() instanceof String key)) {
					throw new IOException(name + " contains a non-string key in " + file);
				}
				mapping.put(key, entry.getValue());
			}
			mappings.add(mapping);
		}
		return mappings;
	}

	private static Map<?, ?> readMapping(Object value, String name, Path file) throws IOException {
		if (!(value instanceof Map<?, ?> mapping)) {
			throw new IOException("Expected a mapping for " + name + " in " + file);
		}
		return mapping;
	}

	private static List<?> readList(Object value, String name, Path file) throws IOException {
		if (!(value instanceof List<?> list)) {
			throw new IOException("Expected a list for " + name + " in " + file);
		}
		return list;
	}

	private static String readString(Object value, String name, Path file) throws IOException {
		if (!(value instanceof String string)) {
			throw new IOException("Expected a string for " + name + " in " + file);
		}
		return string;
	}

	record Fixture(List<Map<String, Object>> tracks, Map<String, ExpectedResult> expectedResults,
			Map<String, String> process) {
	}

	record ExpectedResult(String status, String value) {
	}
}
