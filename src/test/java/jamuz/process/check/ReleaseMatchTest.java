package jamuz.process.check;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReleaseMatchTest {

    // ------------------------------------------------------------------ Track

    @Test
    void track_getTrackNoFull_formatsWithZeroPad() {
        ReleaseMatch.Track t = new ReleaseMatch.Track(1, 2, 3, 10, "Artist", "Title", 200_000L, "CD");
        assertEquals("03/10", t.getTrackNoFull());
    }

    @Test
    void track_getDiscNoFull_formatsWithZeroPad() {
        ReleaseMatch.Track t = new ReleaseMatch.Track(2, 3, 1, 12, "Artist", "Title", 180_000L, "CD");
        assertEquals("02/03", t.getDiscNoFull());
    }

    @Test
    void track_getters_returnConstructorValues() {
        ReleaseMatch.Track t = new ReleaseMatch.Track(1, 1, 5, 8, "Bob", "Song", 300_000L, "Vinyl");
        assertEquals(5, t.getTrackNo());
        assertEquals(8, t.getTrackTotal());
        assertEquals(1, t.getDiscNo());
        assertEquals(1, t.getDiscTotal());
        assertEquals("Bob", t.getArtist());
        assertEquals("Song", t.getTitle());
        assertEquals(300_000L, t.getDuration());
        assertEquals("Vinyl", t.getMedium());
    }

    // ------------------------------------------------------- ReleaseMatch core

    private ReleaseMatch makeOriginal(int score, String artist, String album, String year, int trackTotal) {
        return new ReleaseMatch(score, "Original", artist, album, year, trackTotal, 1);
    }

    @Test
    void original_constructor_isOriginalFlagSetOnlyWhenScoreNegative() {
        ReleaseMatch neg = makeOriginal(-1, "A", "B", "2000", 10);
        assertTrue(neg.isOriginal());

        ReleaseMatch pos = makeOriginal(80, "A", "B", "2000", 10);
        assertFalse(pos.isOriginal());
    }

    @Test
    void original_getters_returnConstructorValues() {
        ReleaseMatch r = makeOriginal(90, "Beatles", "Abbey Road", "1969", 17);
        assertEquals("Beatles", r.getArtist());
        assertEquals("Abbey Road", r.getAlbum());
        assertEquals("1969", r.getYear());
        assertEquals(90, r.getScore());
        assertEquals(17, r.getTrackTotal());
    }

    // ------------------------------------------------- getYearInt edge cases

    @Test
    void getYearInt_validYear_returnsInteger() {
        ReleaseMatch r = makeOriginal(80, "A", "B", "1999", 5);
        assertEquals(1999, r.getYearInt());
    }

    @Test
    void getYearInt_blankYear_returnsMaxValue() {
        ReleaseMatch r = makeOriginal(80, "A", "B", "", 5);
        assertEquals(Integer.MAX_VALUE, r.getYearInt());
    }

    @Test
    void getYearInt_invalidYear_returnsMaxValue() {
        ReleaseMatch r = makeOriginal(80, "A", "B", "not-a-year", 5);
        assertEquals(Integer.MAX_VALUE, r.getYearInt());
    }

    // ------------------------------------------------- compareTo / sort order

    @Test
    void compareTo_higherScoreComesFirst() {
        ReleaseMatch high = makeOriginal(90, "A", "B", "2000", 5);
        ReleaseMatch low  = makeOriginal(50, "A", "B", "2000", 5);
        List<ReleaseMatch> list = new ArrayList<>();
        list.add(low);
        list.add(high);
        Collections.sort(list);
        assertEquals(90, list.get(0).getScore(), "Higher score should sort first");
    }

    @Test
    void compareTo_equalScore_earlierYearComesFirst() {
        ReleaseMatch early = makeOriginal(80, "A", "B", "1969", 5);
        ReleaseMatch late  = makeOriginal(80, "A", "B", "1990", 5);
        List<ReleaseMatch> list = new ArrayList<>();
        list.add(late);
        list.add(early);
        Collections.sort(list);
        assertEquals("1969", list.get(0).getYear(), "Earlier year should sort first when score is equal");
    }

    @Test
    void compareTo_equalScoreAndYear_stableOrder() {
        ReleaseMatch a = makeOriginal(80, "A", "B", "1990", 5);
        ReleaseMatch b = makeOriginal(80, "C", "D", "1990", 5);
        assertEquals(0, a.compareTo(b));
    }

    // ------------------------------------------------- toString formatting

    @Test
    void toString_includesArtistAndAlbum() {
        ReleaseMatch r = makeOriginal(75, "Beatles", "Abbey Road", "1969", 17);
        String s = r.toString();
        assertTrue(s.contains("Beatles"), "toString should contain artist");
        assertTrue(s.contains("Abbey Road"), "toString should contain album");
    }

    @Test
    void toString_includesYearWhenNotBlank() {
        ReleaseMatch r = makeOriginal(75, "A", "B", "1969", 5);
        assertTrue(r.toString().contains("1969"));
    }

    @Test
    void toString_excludesYearBracketWhenYearIsBlank() {
        ReleaseMatch r = makeOriginal(75, "A", "B", "", 5);
        String s = r.toString();
        // Score bracket [75] will be present, but no 4-digit year bracket
        assertFalse(s.matches(".*\\[\\d{4}\\].*"), "No 4-digit year bracket expected when year is blank");
    }

    @Test
    void toString_includesTrackCount() {
        ReleaseMatch r = makeOriginal(75, "A", "B", "2000", 12);
        assertTrue(r.toString().contains("12 tracks"));
    }

    @Test
    void toString_negativeScore_usesRedHtmlColor() {
        ReleaseMatch r = makeOriginal(-1, "A", "B", "2000", 5);
        String s = r.toString();
        assertTrue(s.contains("#ff0000"), "Negative score should render in red");
    }

    @Test
    void toString_score100_usesDarkGreenHtmlColor() {
        ReleaseMatch r = makeOriginal(100, "A", "B", "2000", 5);
        String s = r.toString();
        assertTrue(s.contains("#005F00"), "Score 100 should render in dark green");
    }
}
