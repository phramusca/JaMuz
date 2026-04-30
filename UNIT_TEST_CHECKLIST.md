# JaMuz unit tests — remaining plan only

This checklist intentionally contains **only pending work**.
All completed batch history has been removed.

---

## Current state (as of 2026-04-30)

- Dedicated unit-test coverage for `src/main/java`: **complete**
  - `main_without_dedicated_test`: **0**
  - `main_with_dedicated_test`: **236**
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

### Done (2026-04-30)

- **`FolderInfoResult`** — 26 tests: `colorField`, `formatNumber`, error-level state machine
  (`setKO/setWarning/setOK/restoreFolderErrorLevel`), `analyseTrack` (string / year / number),
  display color, `equals`/`hashCode`/`toString`.
- **`ReleaseMatch`** — 16 tests: `Track` getters and zero-padded formatting (`getTrackNoFull`,
  `getDiscNoFull`), original constructor, `getYearInt` edge cases (blank / invalid / valid),
  `compareTo` sort semantics (score desc, year asc), `toString` color/content rules.
- **`FileInfoVideo`** — 11 tests: quality detection thresholds (HD1080 / HD720 / SD / UNKNOWN),
  `isHD`, `getVideoStreamDetails`, `getFormattedEpisodeNumber`, audio/subtitles stream details.

### Remaining focus areas

1. `jamuz.process.check` — still to deepen:
   - `ProcessCheck`, `FolderInfo` (DB-heavy; skip or use in-memory SQLite), `CheckDisplay` (Swing-wired; keep contract only).
2. `jamuz.process.video` — still to deepen:
   - `VideoAbstract` (Status transitions if any are pure), `DbConnVideo` (needs in-memory DB).
3. `jamuz.gui` and Swing-heavy classes:
   - Keep to non-UI-thread-safe unit checks only.

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
