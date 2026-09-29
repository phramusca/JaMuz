# Functional test fixtures

Fixtures are grouped first by the functional test class that consumes them,
then by scenario. A release fixture is named after its MusicBrainz release ID.
Scenarios used by more than one test class are copied into each suite so each
suite's inputs are self-contained:

```text
albumFiles_yaml/
  CheckNTest/
    CheckTest1_KO/
      <release-id>.yaml
  MergeNTest/
    MergeDevice1_KO/
      <release-id>.yaml
  MergeCheckNTest/
    MergeDevice1_KO/
      <release-id>.yaml
  Merge1Test/
    MergeTest1_Creation/
      <release-id>.yaml
  REFERENCE/
    MusicBrainz_REFERENCE_DO_NOT_MODIFY/
      <release-id>.yaml
  TEMPLATE/
    mod.yaml
    Feuille2.yaml
```

Functional fixture documents use `formatVersion: 2`. After the release and
scenario metadata, their semantic sections appear in this order: `process`
contains the scenario's test setup values; `expectedResults` maps each result
key to its expected status and value; and `tracks`, last because its length
varies, is the ordered list of track metadata. Cell values remain strings to
preserve the ODS data, except the optional per-track `ignore` flag, which is a
boolean.

The MusicBrainz reference sheets and template workbook are not test scenarios;
the former use the same semantic fixture schema, while the template remains
represented as raw cell rows because its worksheets do not share that schema.
The original ODS workbooks remain alongside these YAML fixtures as read-only
references; functional test execution loads YAML only.
