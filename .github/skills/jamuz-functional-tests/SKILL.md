---
name: jamuz-functional-tests
description: Expert workflow for diagnosing, maintaining, and extending JaMuz Swing/Maven functional tests and their YAML album fixtures. Use for Check, Merge, Sync, fixture updates, or functional-test failures.
---

# JaMuz Functional Tests

Use this skill when changing or diagnosing JaMuz functional tests, especially `CheckNTest`, `MergeNTest`, `MergeCheckNTest`, and `Merge1Test`. Work from the `JaMuz/` Maven module root. Preserve strict behavioral assertions and prefer one grouped diagnostic run over repeated runs that stop at the first mismatch.

## Repository map

- Functional test classes: `src/test/java/tests/functional/`
- Test infrastructure: `src/test/java/test/helpers/`
- YAML fixtures: `testenv/Ressources/albumFiles_yaml/<suite>/<scenario>/<releaseId>.yaml`
- Audio/source fixtures and app settings: `testenv/`
- Test overview and commands: `FUNCTIONAL_TEST_PLAN.md`
- YAML parser and invariant checks: `FixtureYaml.java`, `FixtureYamlTest.java`
- Album fixture loading and assertions: `Album.java`, `AlbumBuffer.java`
- Functional process orchestration: `TestSettings.java`, `TestProcessHelper.java`

Before editing, inspect the target test class, the relevant helpers, its complete scenario fixture set, and the current working tree. Do not overwrite user changes or unrelated generated data.

## Execution and runtime facts

Run commands from the module root:

```sh
mvn -Dtest=tests.functional.CheckNTest test
mvn -Dtest=tests.functional.CheckNTest,test.helpers.FixtureYamlTest test
```

Replace the test class with the suite under investigation. `FixtureYamlTest` is the structural regression gate and should accompany fixture changes. Functional tests start a real Swing GUI and use a test SQLite database and files; run them in a graphical environment. Do not use `mvn test` as a proxy when only one slow functional scenario is under change.

NetBeans may run a selected JUnit 5 class; if it reports zero tests, first check the selected class, JUnit Platform/Surefire configuration, and Maven working directory. A successful Maven run of `-Dtest=<fully-qualified-class> test` confirms test discovery. Old missing local dependency-POM warnings may coexist with a successful test run; distinguish those warnings from the test outcome.

Expect external MusicBrainz and Last.fm lookups to make scenarios slow and potentially time-sensitive. A recent full `CheckNTest` took about 3m20s; collect and correct all known differences before rerunning it.

## Fixture contract

Each fixture has the canonical top-level order:

1. `formatVersion`, `releaseId`, `scenario`
2. `process`
3. `expectedResults`
4. `tracks`

Keep blank lines between top-level sections. Fixed-size process/result sections precede the variable-length tracks list for readable diffs.

- `formatVersion` is currently `2`; `releaseId` and `scenario` must match the path and filename.
- `process` stores test execution context (e.g. scan index, database path ID, location). It is consumed by `Album`; Check scan order is also guarded by `FixtureYamlTest`. Never change indices based only on alphabetical order or assumption—verify the observed scan order.
- Every expected result has a `status` (`ok`, `warning`, or `ko`) and a string `value`. In `Album.checkActionsTableModel`, an empty expected value skips comparing that result. `{N/A}` and `{IGNORE}` are ordinary literal strings, not wildcards; use an empty value only when that assertion is intentionally not applicable.
- `tracks` is ordered. Track fields are strings except optional `ignore`, which is a boolean. Preserve exact punctuation, Unicode, capitalization, track/disc numbering, totals, and list order. Exact comparison is intentional.
- Keep each scenario snapshot synchronized when behavior flows through multiple states. In `CheckNTest`, inspect `CheckTest1_KO`, `CheckTest2_OK`, and `CheckTest3_DbOk` for each changed release. Change only the suite/scenarios in scope; `REFERENCE` fixtures are explicitly protected reference data.
- `FixtureYamlTest` asserts suite fixture counts, valid scenario placement, YAML section ordering/spacing, parser validity, result/process counts, and remembered `CheckNTest` ordering and metadata invariants. Update its expected counts or invariants only when the fixture inventory or intended behavior really changes.

## Grouped diagnosis: required workflow

1. **Map the flow.** Identify each scan/merge/sync phase, the fixtures loaded at that phase, and where each comparison occurs. Avoid changing production behavior until evidence points to a production defect.
2. **Collect failures together.** For a scenario with multiple albums, use `assertAll` at the smallest safe level (fields/tracks within album), then collect album/phase failures and report them together at scenario end. Preserve the original assertion messages and attach album ID plus phase. Keep structural/flow guards (such as scanned album counts, DB setup, or failed `applyChanges`) fail-fast when continuing would make subsequent observations invalid.
3. **Run once and classify.** Build a mismatch table: release ID, phase/snapshot, track index/name, field/result key, expected, actual, and likely class (external data drift, fixture stale, application defect, environment/timing). Do not fix only the first assertion and rerun.
4. **Verify the cause.** Reproduce selection/date/status from application logic or response data. Distinguish MusicBrainz from Last.fm; Last.fm may have no year and therefore legitimately produce an unknown-year warning. An ID switch caused by the documented release-selection rules should update only test data and the corresponding snapshots, not weaken selection logic.
5. **Batch the fixture changes.** Make surgical, justified updates to all affected fields/snapshots at once. Do not normalize away a surprising difference until its source is understood. In particular, verify punctuation/case/accents and contributor artist credits; they are often visible in written ID3 tags or filenames.
6. **Validate once after the batch.** Run the targeted functional scenario plus `FixtureYamlTest`. Read the full final report. If a new distinct issue appears, classify it before another run; no blind edit/retry loop.
7. **Audit side effects.** Inspect the exact files the test touched. Functional runs may change the timestamp comment at the top of `testenv/AppFolder/JaMuz.properties` and create audio/database artifacts under test-only locations. Restore only known test-generated changes. Never recursively delete broad directories, user data, or ambiguous files.

### Evidence rules for remote metadata

- Record which release and provider JaMuz actually selected. Search results from a direct MusicBrainz request alone do not prove that JaMuz selected that result; Last.fm matches can also participate.
- A changed release ID, year, track title, contributor, or capitalization may be legitimate remote-data drift. Verify the score/date/order rules in JaMuz before changing the expected data.
- A missing year and an incorrect year are different cases. Do not force a year expectation onto a result whose selected provider has no year.
- Do not infer that two similar-looking UUIDs refer to the same album. Keep IDs named explicitly in test code, fixture paths, and reports.
- Do not access external services repeatedly per field. Gather local mismatch evidence first and make at most the needed targeted external checks.

## Adding another functional scenario

1. Read the test class end-to-end and trace the helper calls, database IDs, paths, generated filenames, and each lifecycle phase.
2. Create fixtures under the correct suite and one directory per scenario, following the YAML contract. If an album participates in multiple tests, duplicate its fixture in each test suite rather than coupling tests through shared mutable data.
3. Add/update test code and fixtures as a coherent change. Make validation cover all scenarios and ensure fixture counts, scenario ownership, ordering rules, and remembered state are correct.
4. Run the new scenario alone, then its fixture validation. If the scenario changes shared helpers, run the smallest neighboring functional test that exercises the changed behavior.
5. Report exactly what was run, elapsed time, tests/failures, remote-service dependencies, and any known nondeterminism. Do not claim the test passes if only YAML parsing passed.

## Completion checklist

- [ ] Production logic was changed only when the evidence demonstrates an application defect.
- [ ] The failure report groups all independent album/track/result mismatches and retains useful context.
- [ ] Every state snapshot and relevant fixture is consistent; unrelated suites/reference data are untouched.
- [ ] Fixture structure, ordering, IDs, values and counts pass `FixtureYamlTest`.
- [ ] The target functional scenario passes from the correct working directory and a GUI environment.
- [ ] Test-generated settings/artifacts were inspected and only known test changes were restored/cleaned.
- [ ] `FUNCTIONAL_TEST_PLAN.md` is updated if test commands, suite flow, fixture contract, or setup changed.
