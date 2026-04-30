# JaMuz unit tests — remaining plan only

This checklist intentionally contains **only pending work**.
All completed batch history has been removed.

---

## Current state (as of 2026-04-30)

- Dedicated unit-test coverage for `src/main/java`: **complete**
  - `main_without_dedicated_test`: **0**
  - `main_with_dedicated_test`: **236**
- All unit tests run with JUnit5 (no more silent JUnit4 skipping): **506 tests, 0 failures, 4 skipped** (network/GUI)
- Unit-test quality counters:
  - `nb_prototype_stubs`: **0**
  - `nb_fixme_test`: **0**
  - `nb_ignore`: **0**
  - `nb_system_out`: **0**
- Non-unit tests tracked separately:
  - `tests/functional/CheckNTest.java`
  - `tests/functional/Merge1Test.java`
  - `tests/functional/MergeCheckNTest.java`
  - `tests/functional/MergeNTest.java`

---

## Remaining goals

1. Keep unit coverage complete while improving test signal quality.
2. Increase depth on high-value classes where current tests are contract-level only.
3. Keep scope pragmatic: avoid brittle over-mocking and GUI-heavy overkill.

---

## Priority 1 — Add depth where value is highest (without overkill)

Most classes now have a dedicated test, but many recent ones are intentionally lightweight contract tests.
Next step is **selective depth**, not blanket expansion.

### Done (2026-04-30, batch 1)

- **`FolderInfoResult`** — 26 tests: `colorField`, `formatNumber`, error-level state machine
  (`setKO/setWarning/setOK/restoreFolderErrorLevel`), `analyseTrack` (string / year / number),
  display color, `equals`/`hashCode`/`toString`.
- **`ReleaseMatch`** — 16 tests: `Track` getters and zero-padded formatting (`getTrackNoFull`,
  `getDiscNoFull`), original constructor, `getYearInt` edge cases (blank / invalid / valid),
  `compareTo` sort semantics (score desc, year asc), `toString` color/content rules.
- **`FileInfoVideo`** — 11 tests: quality detection thresholds (HD1080 / HD720 / SD / UNKNOWN),
  `isHD`, `getVideoStreamDetails`, `getFormattedEpisodeNumber`, audio/subtitles stream details.

### Done (2026-04-30, batch 2)

- **`StringManager`** — 30 tests: `removeIllegal` (illegal chars, consecutive, clean input),
  `truncate` (short / at-limit / over-limit), `getNullableText` (null / non-null / empty),
  `humanReadableByteCount` (SI / binary / negative), `secondsToMMSS` / `secondsToHHMM` (negative
  / zero / values), `humanReadableSeconds` (zero / seconds / minutes / hours / days),
  `parseSlashList` (single / multiple / no-spaces), `Left` / `Right`.
- **`VideoAbstract.Status`** — 5 tests: initial state, `set()` message content, separator
  accumulation, multiple-set chaining.

### Done (2026-04-30, batch 3)

- **`FileInfo`** — 17 tests (JUnit4→JUnit5): `setPath` path decomposition (components, ext lowercased,
  no-ext), `setExt`, `setFilename` rebuild, `compareTo` alphabetical sort, `equalsStats`
  (same/rating/playCounter/genre).  
  Note: the file was JUnit4 (not run in full suite) — converted to JUnit5 as part of this batch.
- **`Results`** — 3 tests: added `getBest()` with `status="ok"` but empty results → null
  (previously only tested error status and chromaprint getter).

### JUnit4 → JUnit5 conversion: COMPLETE (2026-04-30)

All previously-silent JUnit4 test files have been converted to JUnit5 in 5 commits:

1. **Utils purs** — DateTime, Encryption, OS, Inter, Benchmark, AppVersionCheck, QRCode, ClipboardImage
2. **Soulseek data models** — SlskdSearchFile, SlskdSearchResponse, SlskdDockerSharedDir, TableModelSlskd*
3. **Database (39 files)** — all DAO + DbConn + DbInfo + DbVersion + StatSource  
   _Production bugs fixed: `getGeneratedKeys()` → `last_insert_rowid()`, `DbConn.disconnect()` NPE,  
   AUTOINCREMENT reset, path normalization, client name insert-suffix vs update_
4. **Utils restants** — ClipboardText, ProcessAbstract, FileSystem, Popup, Swing, Desktop, SSH, Ftp
5. **AppVersionTest** (Mockito JUnit5) + IconBufferVideoTest

Infrastructure additions:
- `Jamuz.setMachine(Machine)` — allows tests to inject a minimal Machine without full app init
- `TestUnitSettings.setupJamuzGlobals()` — initialises `Jamuz.db` and `Jamuz.machine` for tests needing `FolderInfo`
- commons-io upgraded 2.14.0 → 2.16.1 (required by commons-compress 1.28.0's `ChecksumInputStream`)

### Remaining focus areas

1. `jamuz.process.check` / `jamuz.process.video` DB-heavy classes: skip unless in-memory SQLite
   feasible without major effort.
2. Contract-only classes (`CheckDisplay`, Swing panels): intentionally kept lightweight.
3. `AppVersionCheck` async tests (`@Disabled`): need injectable `OkHttpClient` or base-URL parameter
   to mock the GitHub API endpoint properly.

Definition of “enough depth”:

- At least one meaningful behavior test per public non-trivial method on critical classes.
- Edge cases covered for parsing/formatting/state computations.
- No external network/process dependency in default unit runs.

---

## Priority 2 — Keep functional tests out of unit metrics

Functional tests are valid but should stay in a separate track.

- Keep `tests/functional/*` as `scope=functional` in CSV.
- Do not mix functional stabilization work into unit completion metrics.
- If needed later, create a dedicated functional test plan document.

---

## Operating loop (for future passes)

1. Implement one focused cleanup/deepening batch.
2. Run targeted tests first, then full suite:
   - `mvn -q test -Dtest=...`
   - `mvn -q test`
3. Regenerate tracker:
   - `python3 scripts/generate_unit_test_tracking.py`
4. Verify acceptance gates below.

---

## Acceptance gates for “unit tests complete and clean”

- `main_without_dedicated_test == 0` (already reached).
- Unit scope has:
  - `nb_prototype_stubs == 0`
  - `nb_fixme_test == 0`
  - `nb_ignore == 0`
  - `nb_system_out == 0`
- Full Maven test suite passes in CI-equivalent environment.
- No newly introduced flaky/UI-blocking unit tests.
