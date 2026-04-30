package jamuz.process.check;

import java.awt.Color;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FolderInfoResultTest {

    // --- static helpers ---

    @Test
    void colorField_ok_producesGreenHtml() {
        String html = FolderInfoResult.colorField("hello", 0);
        assertTrue(html.contains("#32cd32"), "OK color should be lime green");
        assertTrue(html.contains("hello"));
        assertTrue(html.startsWith("<html>") && html.endsWith("</html>"));
    }

    @Test
    void colorField_warning_producesOrangeHtml() {
        String html = FolderInfoResult.colorField("warn", 1);
        assertTrue(html.contains("#ffa500"), "Warning color should be orange");
    }

    @Test
    void colorField_ko_producesRedHtml() {
        String html = FolderInfoResult.colorField("err", 2);
        assertTrue(html.contains("#FF0000"), "KO color should be red");
    }

    @Test
    void colorField_nullText_replacedByNullLabel() {
        String html = FolderInfoResult.colorField(null, 0);
        assertTrue(html.contains("{null}"));
    }

    @Test
    void colorField_blankText_replacedByEmptyLabel() {
        String html = FolderInfoResult.colorField("   ", 0);
        assertTrue(html.contains("{Empty}"));
    }

    @Test
    void colorField_noHtmlWrap_doesNotAddHtmlTags() {
        String html = FolderInfoResult.colorField("v", 0, false);
        assertFalse(html.contains("<html>"));
        assertTrue(html.contains("v"));
    }

    @Test
    void formatNumber_singleDigit_zeroPadded() {
        assertEquals("03", FolderInfoResult.formatNumber(3));
    }

    @Test
    void formatNumber_doubleDigit_unchanged() {
        assertEquals("12", FolderInfoResult.formatNumber(12));
    }

    @Test
    void formatNumber_zero_zeroPadded() {
        assertEquals("00", FolderInfoResult.formatNumber(0));
    }

    // --- error level state machine ---

    @Test
    void defaultState_isOk() {
        FolderInfoResult r = new FolderInfoResult();
        assertFalse(r.isKO());
        assertFalse(r.isWarning());
        assertFalse(r.isNotValid());
        assertEquals(0, r.getErrorLevel());
    }

    @Test
    void setWarning_transitionsToWarning() {
        FolderInfoResult r = new FolderInfoResult();
        r.setWarning();
        assertTrue(r.isWarning());
        assertFalse(r.isKO());
        assertTrue(r.isNotValid());
        assertEquals(1, r.getErrorLevel());
    }

    @Test
    void setKO_transitionsToKO() {
        FolderInfoResult r = new FolderInfoResult();
        r.setKO();
        assertTrue(r.isKO());
        assertFalse(r.isWarning());
        assertTrue(r.isNotValid());
        assertEquals(2, r.getErrorLevel());
    }

    @Test
    void setOK_resetsToOkFromKO() {
        FolderInfoResult r = new FolderInfoResult();
        r.setKO();
        r.setOK();
        assertFalse(r.isNotValid());
        assertEquals(0, r.getErrorLevel());
    }

    @Test
    void setWarning_doesNotDowngradeFromKO() {
        FolderInfoResult r = new FolderInfoResult();
        r.setKO();
        r.setWarning();
        assertTrue(r.isKO(), "setWarning should not downgrade an existing KO");
    }

    @Test
    void restoreFolderErrorLevel_undoesMatchKO() {
        FolderInfoResult r = new FolderInfoResult();
        r.setKO(false);
        r.setOK();                  // match setOK (only changes errorLevel, not folder)
        r.restoreFolderErrorLevel();
        assertTrue(r.isKO(), "folder-level KO should be restored");
    }

    // --- display color ---

    @Test
    void getDisplayColor_ok_isGreenFamily() {
        FolderInfoResult r = new FolderInfoResult();
        Color c = r.getDisplayColor();
        assertEquals(new Color(0, 128, 0), c);
    }

    @Test
    void getDisplayColor_warning_isOrange() {
        FolderInfoResult r = new FolderInfoResult();
        r.setWarning();
        assertEquals(Color.orange, r.getDisplayColor());
    }

    @Test
    void getDisplayColor_ko_isRed() {
        FolderInfoResult r = new FolderInfoResult();
        r.setKO();
        assertEquals(Color.red, r.getDisplayColor());
    }

    // --- analyseTrack (string) ---

    @Test
    void analyseTrack_stringMatch_exactSame_isOk() {
        FolderInfoResult r = new FolderInfoResult();
        String display = r.analyseTrack("Beatles", "Beatles", "artist");
        assertFalse(r.isNotValid());
        assertTrue(display.contains("Beatles"));
    }

    @Test
    void analyseTrack_stringMatch_caseOnlyDiffers_isWarning() {
        FolderInfoResult r = new FolderInfoResult();
        r.analyseTrack("Beatles", "beatles", "artist");
        assertTrue(r.isWarning());
        assertFalse(r.isKO());
    }

    @Test
    void analyseTrack_stringMismatch_isKO() {
        FolderInfoResult r = new FolderInfoResult();
        r.analyseTrack("Beatles", "Rolling Stones", "artist");
        assertTrue(r.isKO());
    }

    @Test
    void analyseTrack_emptyStringAndEmptyMatch_isKO() {
        FolderInfoResult r = new FolderInfoResult();
        r.analyseTrack("", "", "title");
        assertTrue(r.isKO(), "empty tag + empty match should be KO");
    }

    // --- analyseTrack (year) ---

    @Test
    void analyseTrack_yearMatch_same_isOk() {
        FolderInfoResult r = new FolderInfoResult();
        r.analyseTrack("1999", "1999", "year");
        assertFalse(r.isNotValid());
    }

    @Test
    void analyseTrack_yearMatch_blankMatchWithValidYear_isWarning() {
        FolderInfoResult r = new FolderInfoResult();
        r.analyseTrack("1999", "", "year");
        assertTrue(r.isWarning());
    }

    @Test
    void analyseTrack_yearMatch_blankMatchWithInvalidYear_isKO() {
        FolderInfoResult r = new FolderInfoResult();
        r.analyseTrack("not-a-year", "", "year");
        assertTrue(r.isKO());
    }

    @Test
    void analyseTrack_yearMismatch_isKO() {
        FolderInfoResult r = new FolderInfoResult();
        r.analyseTrack("1999", "2000", "year");
        assertTrue(r.isKO());
    }

    // --- analyseTrack (track number) ---

    @Test
    void analyseTrack_trackNumberMatch_isOk() {
        FolderInfoResult r = new FolderInfoResult();
        String display = r.analyseTrack("03/10", "03/10", "trackNoFull");
        assertFalse(r.isKO());
        assertTrue(display.contains("03"));
        assertTrue(display.contains("10"));
    }

    @Test
    void analyseTrack_trackNumberMismatch_isKO() {
        FolderInfoResult r = new FolderInfoResult();
        r.analyseTrack("02/10", "03/10", "trackNoFull");
        assertTrue(r.isKO());
    }

    // --- equals / hashCode / toString ---

    @Test
    void equals_sameValueAndLevel_areEqual() {
        FolderInfoResult a = new FolderInfoResult();
        a.setValue("x");
        a.setWarning();
        FolderInfoResult b = new FolderInfoResult();
        b.setValue("x");
        b.setWarning();
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void equals_differentLevel_notEqual() {
        FolderInfoResult a = new FolderInfoResult();
        a.setValue("x");
        FolderInfoResult b = new FolderInfoResult();
        b.setValue("x");
        b.setKO();
        assertNotEquals(a, b);
    }

    @Test
    void toString_containsValueAndLevel() {
        FolderInfoResult r = new FolderInfoResult();
        r.setValue("hello");
        String s = r.toString();
        assertTrue(s.contains("hello"));
        assertTrue(s.contains("0"));
    }
}
