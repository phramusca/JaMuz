# Grouped Fixture Diagnostics

JaMuz functional tests should aggregate independent fixture mismatches before a slow end-to-end rerun.

## What Happened

`CheckNTest` originally stopped at its first field mismatch, making a fixture refresh require repeated long runs. Grouping assertions by track fields, result keys, album, and scenario phase exposed differences across several releases in one run. The evidence included remote metadata drift, punctuation/case changes, missing Last.fm years, and stale persisted-state expectations. The test then passed after a single batched fixture update. Discovered in `JaMuz` during `jamuz-functional-test-skill`.

## Takeaway

Keep strict comparisons, collect independent failures with useful release/phase/track context, and fail fast only when a flow invariant makes later observations invalid. Classify all observed diffs before changing YAML; verify whether a selected match came from MusicBrainz or Last.fm. Update all snapshots for an affected scenario together, run the scenario plus `FixtureYamlTest`, then inspect only known test side effects.

## History

- 2026-09-30 (`JaMuz/jamuz-functional-test-skill`): initial
