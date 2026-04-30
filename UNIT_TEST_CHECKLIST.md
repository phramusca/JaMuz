# JaMuz unit tests — remaining plan only

This checklist intentionally contains **only pending work**.
All completed batch history has been removed.

---

## Current state (as of 2026-04-30)

- Dedicated unit-test coverage for `src/main/java`: **complete**
  - `main_without_dedicated_test`: **0**
  - `main_with_dedicated_test`: **236**
- All unit tests run with JUnit5 (no more silent JUnit4 skipping): **666 tests, 0 failures, 3 skipped** (network/GUI)
- Unit-test quality counters:
  - `nb_prototype_stubs`: **0**
  - `nb_fixme_test`: **0**
  - `nb_ignore`: **0** ✅ (was 3 — AppVersionCheck @Disabled fixed with MockWebServer)
  - `nb_system_out`: **0**
- Non-unit tests tracked separately:
  - `tests/functional/CheckNTest.java`
  - `tests/functional/Merge1Test.java`
  - `tests/functional/MergeCheckNTest.java`
  - `tests/functional/MergeNTest.java`
- Status distribution (test files):
  - `clean`: **115**
  - `rather_clean`: **124** (91 are intentionally lightweight GUI/Swing files)

---

## Architecture improvement: Popup refactoring (COMPLETE)

All non-GUI classes have been cleaned of direct `Popup.*` calls:

- `jamuz/utils/` package (8 files): XML, FileSystem, SSH, OS, QRCode, LogText, ClipboardText, Desktop, Dependencies
- Service/model packages (30 files): database/, FileInfoInt, IconBuffer*, Options, player/, process/book/,
  process/check/, process/merge/, process/video/, soulseek/
- `pom.xml`: headless mode enforced via `-Djava.awt.headless=true` in Surefire config

Remaining intentional `Popup` usage: `jamuz/gui/**`, `Jamuz.java`, `Main.java` (entry points and UI layers — correct).

---

## Priority 1 — Depth expansion (ongoing)

Most classes have a dedicated test; many remain at contract-level.
Focus is **selective depth** on classes with meaningful pure logic.

### Done (batches 1–3, see git log)

- `FolderInfoResult`, `ReleaseMatch`, `FileInfoVideo`, `StringManager`, `VideoAbstract.Status`,
  `FileInfo` (JUnit4→5), `Results`

### Done (batch 4+, 2026-04-30 session)

- **`StatSource`** (7 tests): defaults, isSelected, hidden flag, equals/hashCode
- **`Device`** (5 tests): constructor, setters, equals/hashCode
- **`Playlist`** (5 tests): constructor, setters, compareTo, LimitUnit/Type enums
- **`Option`** (5 tests): all fields, setValue, toString
- **`Keys`** (3 tests): save always false, set is no-op, get returns Missing
- **`IconBuffer`** (4 tests): iconSize, IconVersion enum, getCoverIcon with missing file
- **`IconBufferCover`** (2 tests): getCoverIconSize, NPE contract for null file
- **`MetaFlac`** (2 tests): constructor, process with non-existent path returns false
- **`Recording`** (3 tests): id storage including empty/null
- **`MyTvShow`** (6 tests): id/homepage from TvSeries, getSerie/setSerie, toString contains name
- **`Options`** (6 tests): set/save/read round trip, missing key placeholder, non-writable path
- **`VideoRating`** (3 tests): rating/display accessors, zero/empty edge cases
- **`SlskdDownloadFile`** (3 tests): getKey format, zero size, default fields non-null
- **`SlskdDownloadDirectory`** (2 tests): field read/write, fileCount default
- **`SlskdDownloadUser`** (2 tests): field read/write, directories default null
- **`MyVideoAbstract`** (7 tests): defaults, setIsFavorite/setIsInWatchList/setUserRating + cache calls, getYear parsing
- **`SlskdDocker`** (3 tests): constants, buildSharedDirEnvValue (no-exclude, with-exclude)
- **`FileInfoDisplay`** (4 tests): constructor, default TableValues, clone, isAudioFile
- **`JMPlayer`** (5 tests): constructor, getMPlayerPath, setMPlayerPath, isPlaying default, getPlayingFile default
- **`Book`** (8 tests): constructor, getFormat, isLocal, getTags, isSelected/setSelected, compareTo, toString
- **`MyMovieDb`** (6 tests): id/homepage, getMovieDb/setMovieDb, toString contains title
- **`ReleaseLastFm`** (2 tests): constructor, getCoverList before search is null
- **`ReleaseMB`** (2 tests): constructor, getCoverList before search is null
  - **Bug fix**: `ReleaseMB.getCoverList()` called `Collections.sort(null)` before any search → added null guard

### Remaining `rather_clean` non-GUI classes (30 files)

These are intentionally lightweight contract tests.
Deepen only if clear pure-logic value exists (avoid DB/network/process deps):

| File | nb_tests | Notes |
|---|---|---|
| `jamuz/FileInfoIntTest.java` | 37 | Tag read/write — large class, some coverage exists |
| `jamuz/JamuzTest.java` | 1 | Entry point, hard to unit-test |
| `jamuz/MainTest.java` | 1 | Entry point, hard to unit-test |
| `jamuz/acoustid/AcoustIDTest.java` | 1 | Needs fpcalc binary |
| `jamuz/player/MPlaybackListenerTest.java` | 3 | Interface, already covers method names |
| `jamuz/player/MplayerTest.java` | 1 | External process dependency |
| `jamuz/process/book/DbConnBookTest.java` | 1 | DB-heavy |
| `jamuz/process/book/IconBufferBookTest.java` | 1 | GUI cache |
| `jamuz/process/book/ProcessBookTest.java` | 1 | DB + process |
| `jamuz/process/check/CheckDisplayTest.java` | 1 | Swing-heavy constructor |
| `jamuz/process/check/CoverTest.java` | 7 | Already well covered |
| `jamuz/process/check/FileInfoDuplicateReplaceTest.java` | 1 | Already reasonable |
| `jamuz/process/check/FolderInfoTest.java` | 1 | DB-heavy |
| `jamuz/process/check/LocationTest.java` | 1 | Needs Jamuz.getMachine() |
| `jamuz/process/check/MP3gainTest.java` | 1 | External process |
| `jamuz/process/check/ProcessCheckTest.java` | 1 | DB + process |
| `jamuz/process/merge/ProcessMergeTest.java` | 1 | DB + callbacks |
| `jamuz/process/merge/StatSource*Test.java` | 1 each | Already cover capabilities flags |
| `jamuz/process/sync/ProcessSyncTest.java` | 1 | DB + process |
| `jamuz/process/video/DbConnVideoTest.java` | 1 | DB-heavy |
| `jamuz/process/video/ProcessVideoTest.java` | 1 | DB + process |
| `jamuz/process/video/TheMovieDbTest.java` | 1 | Network-heavy |
| `jamuz/process/video/VideoMovieTest.java` | 1 | Subtype check only |
| `jamuz/process/video/VideoTvShowTest.java` | 1 | Subtype check only |
| `jamuz/remote/ServerTest.java` | 1 | Network port |
| `jamuz/utils/DependenciesTest.java` | 1 | Docker-dependent |

**Recommendation**: most of these have legitimate reasons to stay lightweight. The few worth expanding:
- `VideoMovieTest`, `VideoTvShowTest`: could add constructor/field tests (low effort)
- `MPlaybackListenerTest`: interface already well-tested

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

## Acceptance gates for "unit tests complete and clean"

- `main_without_dedicated_test == 0` (already reached).
- Unit scope has:
  - `nb_prototype_stubs == 0`
  - `nb_fixme_test == 0`
  - `nb_ignore == 0`
  - `nb_system_out == 0`
- Full Maven test suite passes in CI-equivalent environment.
- No newly introduced flaky/UI-blocking unit tests.
