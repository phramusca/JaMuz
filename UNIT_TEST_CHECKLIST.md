# JaMuz unit tests — guide and tracking

This document is intentionally **short and structured**. Per-file detail is in the CSV.

---

## Processed test files (skeletons → real tests)

Update this table with each batch. **code** means a production fix in `src/main` linked to the test file.

| Test file | Notes |
|-----------|--------|
| `jamuz/database/DaoClientWriteTest.java` | + fix in `DaoClientWrite` (`setNull` for device/statSource FKs) |
| `jamuz/database/DaoDeviceFileWriteTest.java` | |
| `jamuz/database/DaoDeviceWriteTest.java` | |
| `jamuz/database/DaoFileTagWriteTest.java` | |
| `jamuz/database/DaoFileTranscodedWriteTest.java` | |
| `jamuz/database/DaoFileWriteTest.java` | |
| `jamuz/database/DaoGenreWriteTest.java` | + fix in `DaoGenre.isSupported` (`ResultSet.next`) |
| `jamuz/database/DaoMachineWriteTest.java` | |
| `jamuz/database/DaoOptionWriteTest.java` | |
| `jamuz/database/DaoPathWriteTest.java` | |
| `jamuz/database/DaoPathAlbumWriteTest.java` | (class has no write API yet — construction only) |
| `jamuz/database/DaoPlayCounterWriteTest.java` | |
| `jamuz/database/DaoPlaylistWriteTest.java` | |
| `jamuz/database/DaoSchemaWriteTest.java` | no-op schema version only (avoids GUI upgrade path) |
| `jamuz/database/DaoStatSourceWriteTest.java` | + fix in `DaoStatSourceWrite.getLastMergeDateFromDatabase` (`ResultSet.next`) |
| `jamuz/database/DaoTagWriteTest.java` | |
| `jamuz/database/DbInfoTest.java` | |
| `jamuz/database/DaoOptionTest.java` | |
| `jamuz/database/DaoClientTest.java` | |
| `jamuz/database/DaoDeviceFileTest.java` | |
| `jamuz/database/DaoDeviceTest.java` | |
| `jamuz/database/DaoFileTagTest.java` | |
| `jamuz/database/DaoFileTest.java` | |
| `jamuz/database/DaoFileTranscodedTest.java` | |
| `jamuz/database/DaoGenreTest.java` | |
| `jamuz/database/DaoListModelTest.java` | |
| `jamuz/database/DaoMachineTest.java` | |
| `jamuz/database/DaoPathAlbumTest.java` | |
| `jamuz/database/DaoPathTest.java` | |
| `jamuz/database/DaoPlayCounterTest.java` | |
| `jamuz/database/DaoPlaylistTest.java` | |
| `jamuz/database/DaoSchemaTest.java` | |
| `jamuz/database/DaoStatSourceTest.java` | |
| `jamuz/database/DaoTagTest.java` | |
| `jamuz/database/DbVersionTest.java` | |
| `jamuz/database/StatSourceSQLTest.java` | |
| `jamuz/database/StatSourceAbstractTest.java` | |
| `jamuz/database/DbConnJaMuzTest.java` | |
| `jamuz/database/DbConnTest.java` | |
| `jamuz/FileInfoIntTest.java` | FIXME cleanup + coverage additions (JUnit5) |
| `jamuz/utils/DateTimeTest.java` | removed println noise, kept deterministic assertions |
| `jamuz/OptionTest.java` | new JUnit5 dedicated test |
| `jamuz/StatItemTest.java` | new JUnit5 dedicated test |
| `jamuz/KeysTest.java` | new JUnit5 dedicated test |
| `jamuz/LogFormatTest.java` | new JUnit5 dedicated test |
| `jamuz/process/sync/SyncStatusTest.java` | new JUnit5 dedicated test |
| `jamuz/acoustid/AcoustIdResultTest.java` | new JUnit5 dedicated test |
| `jamuz/acoustid/ChromaPrintTest.java` | new JUnit5 dedicated test |
| `jamuz/acoustid/RecordingTest.java` | new JUnit5 dedicated test |
| `jamuz/acoustid/ResultTest.java` | new JUnit5 dedicated test |
| `jamuz/acoustid/ResultsTest.java` | new JUnit5 dedicated test |
| `jamuz/soulseek/SlskdDownloadFileTest.java` | new JUnit5 dedicated test |
| `jamuz/soulseek/SlskdDownloadDirectoryTest.java` | new JUnit5 dedicated test |
| `jamuz/soulseek/SlskdDownloadUserTest.java` | new JUnit5 dedicated test |
| `jamuz/process/check/ActionResultTest.java` | new JUnit5 dedicated test |
| `jamuz/process/video/VideoRatingTest.java` | new JUnit5 dedicated test |
| `jamuz/gui/swing/FileSizeComparableTest.java` | new JUnit5 dedicated test |
| `jamuz/utils/UtilsTest.java` | new JUnit5 dedicated test |
| `jamuz/soulseek/ICallBackSearchTest.java` | JUnit5 contract/reflection check |
| `jamuz/remote/ICallBackServerTest.java` | JUnit5 contract/reflection check |
| `jamuz/ICallBackVersionUpdateTest.java` | JUnit5 contract/reflection check |
| `jamuz/ICallBackVersionCheckTest.java` | JUnit5 contract/reflection check |
| `jamuz/gui/ICallBackSelectTest.java` | JUnit5 contract/reflection check |
| `jamuz/process/check/ICallBackReCheckTest.java` | JUnit5 contract/reflection check |
| `jamuz/process/check/ICallBackReplaceTest.java` | JUnit5 contract/reflection check |
| `jamuz/process/check/ICallBackCheckPanelTest.java` | JUnit5 contract/reflection check |
| `jamuz/process/check/ICallBackDuplicatePanelTest.java` | JUnit5 contract/reflection check |
| `jamuz/process/check/ICallBackScannerTest.java` | JUnit5 contract/reflection check |
| `jamuz/process/check/ICallBackCoverTest.java` | JUnit5 contract/reflection check |
| `jamuz/process/check/ICallBackDuplicateDialogTest.java` | JUnit5 contract/reflection check |
| `jamuz/process/merge/ICallBackMergeTest.java` | JUnit5 contract/reflection check |
| `jamuz/gui/PopupMenuListenerTest.java` | JUnit5 contract test |
| `jamuz/player/MPlaybackListenerTest.java` | JUnit5 contract test |
| `jamuz/process/sync/ICallBackSyncTest.java` | JUnit5 contract test |
| `jamuz/process/check/CoverMBTest.java` | JUnit5 inherited behavior test |
| `jamuz/gui/swing/ProgressCellRenderTest.java` | JUnit5 renderer return contract |
| `jamuz/gui/swing/TableValueTest.java` | JUnit5 value/display behavior |
| `jamuz/gui/swing/TableHorizontalTest.java` | JUnit5 viewport width behavior |
| `jamuz/gui/SchemaUpgradeProgressTest.java` | JUnit5 no-crash lifecycle test |
| `jamuz/gui/swing/CheckBoxListItemTest.java` | JUnit5 selection and payload behavior |
| `jamuz/gui/swing/ComboBoxRendererTest.java` | JUnit5 icon selection behavior |
| `jamuz/database/SchemaUpgradeLogTest.java` | JUnit5 file logging behavior |
| `jamuz/gui/swing/ListElementTest.java` | JUnit5 equality/hash/clone behavior |
| `jamuz/gui/swing/PopupListenerTest.java` | JUnit5 non-popup mouse event behavior |
| `jamuz/gui/swing/TableModelGenericTest.java` | JUnit5 abstract model contract |
| `jamuz/gui/swing/PasswordFieldWithToggleTest.java` | JUnit5 text/password state behavior |
| `jamuz/gui/swing/ButtonBrowseURLTest.java` | JUnit5 editor value behavior |
| `jamuz/process/video/ButtonOpenVideoTest.java` | JUnit5 editor value behavior |
| `jamuz/process/merge/StatSourceJaMuzRemoteTest.java` | JUnit5 source flags and no-op behavior |
| `jamuz/process/merge/StatSourceMediaMonkeyTest.java` | JUnit5 source flags and unsupported tags |
| `jamuz/process/merge/StatSourceMyTunesTest.java` | JUnit5 source flags and unsupported tags |
| `jamuz/gui/swing/ButtonProcessTest.java` | JUnit5 constructor/state behavior |
| `jamuz/gui/swing/CheckBoxListTest.java` | JUnit5 default model/selection behavior |
| `jamuz/utils/LogTextTest.java` | JUnit5 log file create/write behavior |
| `jamuz/IconBufferCoverTest.java` | JUnit5 constant behavior |
| `jamuz/gui/swing/ProgressBarTest.java` | JUnit5 progress string/value behavior |
| `jamuz/OptionsTest.java` | JUnit5 read/save property behavior |
| `jamuz/utils/XMLTest.java` | JUnit5 XML utility behavior |
| `jamuz/gui/swing/SortedListModelTest.java` | JUnit5 sorted model operations |
| `jamuz/process/check/FileInfoDuplicateReplaceTest.java` | JUnit5 duplicate-replace data behavior |
| `jamuz/process/check/PatternProcessorTest.java` | JUnit5 pattern extraction behavior |
| `jamuz/process/merge/StatSourceKodiTest.java` | JUnit5 source flags and unsupported tags |
| `jamuz/acoustid/AcoustIDTest.java` | JUnit5 fpcalc error-path behavior |
| `jamuz/gui/swing/ListModelSelectorTest.java` | JUnit5 empty model behavior |
| `jamuz/gui/ListCellRendererGenreTest.java` | JUnit5 renderer text behavior |
| `jamuz/gui/swing/ListCellRendererSelectorTest.java` | JUnit5 renderer selector behavior |
| `jamuz/process/video/TableCellRendererTooltipTest.java` | JUnit5 tooltip binding behavior |
| `jamuz/process/book/IconBufferBookTest.java` | JUnit5 icon-buffer constants/cache miss behavior |
| `jamuz/process/book/BookTest.java` | JUnit5 book metadata and comparison behavior |
| `jamuz/process/book/TableRowFilterBookTest.java` | JUnit5 default row-filter include behavior |
| `jamuz/process/video/MyVideoAbstractTest.java` | JUnit5 flags/rating/year helper behavior |

*Table last updated: 2026-04-28 (late night).*

---

## 1. Which files to use

| File | Role |
|------|------|
| **`UNIT_TEST_TRACKING.csv`** | Unified tracking with both `*Test.java` rows and `src/main` class rows in the same file. |
| **`UNIT_TEST_CHECKLIST.md`** (this file) | Execution notes and batch history. |

**Regenerate the CSV** from `JaMuz/`:

```bash
python3 scripts/generate_unit_test_tracking.py
```

---

## 2. CSV columns (`UNIT_TEST_TRACKING.csv`)

Separator: **semicolon** (`;`).

| Column | Meaning |
|--------|---------|
| `entry_type` | `test` or `main_class`. |
| `file` | Relative path under `src/test/java` or `src/main/java`. |
| `linked_class_or_test` | For tests: linked main class. For main classes: linked `*Test.java` when it exists. |
| `scope` | `unit`, `functional`, or `missing_test` (for uncovered main classes). |
| `junit` | `junit4`, `junit5`, `none`, or `unknown`. |
| `nb_tests`..`nb_system_out` | Counters (meaningful for test rows). |
| `status` | Lifecycle state for test quality or class coverage. |
| `harmonize` | Standardization guidance. |
| `gaps_or_actions` | Next action. |

---

## 3. Status lexicon

| Value | Meaning |
|--------|---------|
| `rather_clean` | Test row appears clean at tracker level. |
| `to_complete_fixme` | Test still contains FIXME work. |
| `review_logs` | Test still has noisy `System.out.println`. |
| `blocked_prototypes` / `mixed_prototypes` | Legacy prototype stubs remain. |
| `out_of_unit_scope` | Functional test row (`tests/functional`). |
| `main_with_dedicated_test` | Main class has a same-name dedicated test class. |
| `main_without_dedicated_test` | Main class still lacks a dedicated test class (priority for new JUnit5 tests). |

---

## 4. Global summary (existing analysis)

### 4.1 What exists today

- **Location**: `JaMuz/src/test/java/`, mainly `jamuz.database` (large volume), then `jamuz.utils`, `jamuz.soulseek`, `jamuz` (root), `jamuz.process`, and **`tests/functional`** (non-unit scope in the CSV).
- **Maven**: Surefire includes only `**/*Test.java` (`pom.xml`). Helper classes are not run by default.
- **JUnit**: almost everything is **JUnit 4**; **one** class is **JUnit 5**: `jamuz/FileInfoIntTest.java` — decide project-wide harmonisation.
- **Rough counts** (snapshot when the CSV was generated): **~177** prototype stubs, mostly in `jamuz.database`; exact counts per file = column `nb_squelettes_prototype` in the CSV.

### 4.2 Harmonise (cross-cutting)

1. **NetBeans stubs**: replace with a minimal useful test, or remove / `@Ignore` with a **one-line reason** (avoid pointless red `mvn test`).
2. **Naming**: prefer explicit method names (e.g. `shouldRejectWhenLoginNull`); rename **when touching a file**.
3. **`System.out.println`**: reduce in tests (especially `database`, `utils`) in favour of assertions or configurable logging.
4. **JUnit 4 vs 5**: either migrate `FileInfoIntTest` to JUnit 4, or plan Jupiter adoption — **one decision** avoids long-term mixing.
5. **`jamuz.utils`**: many tests are **OS / network / UI sensitive**; treat as **integration** or document per file in the CSV (`harmoniser` column).

### 4.3 Gaps (cross-cutting)

1. **DAO / DB**: negative cases, SQL constraints, uncovered methods — often already flagged by `FIXME TEST` (`nb_fixme_test`).
2. **Models**: `FileInfoIntTest` — getters/setters / paths noted in FIXMEs.
3. **Test helpers**: e.g. `TestProcessHelper` (not `*Test.java`: **absent from CSV**; track separately or rename to `*Test` if you want Surefire to pick it up).

---

## 5. Suggested work order (everything eventually)

1. `jamuz/database/*Test.java` — highest impact (stubs + FIXME).
2. `jamuz/FileInfoIntTest.java` + `FileInfoTest.java`.
3. `jamuz/soulseek/*Test.java` — often already `plutot_propre` (verify in CSV).
4. `jamuz/utils/*Test.java` — clarify unit vs integration.
5. `tests/functional` — separate plan.

For **per-file detail**, open **`UNIT_TEST_TRACKING.csv`** and sort/filter on `etat_synthese` or `perimetre`.
