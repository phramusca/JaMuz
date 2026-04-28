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

*Table last updated: 2026-04-28.*

---

## 1. Which files to use

| File | Role |
|------|------|
| **`UNIT_TEST_TRACKING.csv`** | Tracking: **one row per** `*Test.java` under `src/test/java`. Auto columns + “harmonise” / “gaps” hints. |
| **`UNIT_TEST_CHECKLIST.md`** (this file) | How to read the CSV, status lexicon, **global** summary (harmonisation / gaps). |

**Regenerate the CSV** (counters and status), from the `JaMuz/` directory:

```bash
python3 scripts/generate_unit_test_tracking.py
```

---

## 2. CSV columns (`UNIT_TEST_TRACKING.csv`)

Separator: **semicolon** (`;`) for easy use in LibreOffice / Excel.

| Column | Meaning |
|--------|---------|
| `fichier` | Path relative to `src/test/java/`. |
| `perimetre` | `unitaire` or `fonctionnel` (`tests/functional/`). |
| `junit` | `junit4`, `junit5`, or `inconnu` (unusual imports). |
| `nb_tests` | Methods annotated `@Test` (JUnit 4 or Jupiter). |
| `nb_squelettes_prototype` | Lines with `fail("The test case is a prototype.")` (unfilled NetBeans stubs). |
| `nb_fixme_test` | Occurrences of `FIXME TEST` in the file. |
| `nb_ignore` | Occurrences of `@Ignore`. |
| `nb_system_out` | Occurrences of `System.out.println`. |
| `etat_synthese` | Machine-readable summary (see §3). |
| `harmoniser` | Harmonisation hint **for this file**. |
| `manques_ou_actions` | Coverage / TODO hint **for this file**. |

---

## 3. Lexicon: `etat_synthese`

| Value | Meaning |
|--------|---------|
| `hors_perimetre_unitaire` | Under `tests/functional/`: out of “unit tests only” scope; track separately. |
| `bloque_squelettes` | All (or almost all) `@Test` methods are still prototypes: **priority** replace or remove. |
| `mixte_squelettes` | At least one real test, but stubs remain: clean up so `mvn test` reflects reality. |
| `a_completer_fixme` | Few or no stubs, but `FIXME TEST` items remain. |
| `revue_logs` | Many `println`s: noise and risk for CI / report readability. |
| `plutot_propre` | No strong automated signal; light review if you touch the module. |

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
