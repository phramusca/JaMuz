package jamuz.utils;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StringManagerTest {

    // ---------------------------------------------------------------- Left / Right

    @Test
    void left_returnsLeadingCharacters() {
        assertEquals("ab", StringManager.Left("abcd", 2));
    }

    @Test
    void right_returnsTrailingCharacters() {
        assertEquals("cd", StringManager.Right("abcd", 2));
    }

    // -------------------------------------------------------------- removeIllegal

    @Test
    void removeIllegal_replacesWindowsPathChars() {
        assertEquals("a_b_c", StringManager.removeIllegal("a:b/c"));
    }

    @Test
    void removeIllegal_replacesQuote() {
        assertEquals("a_b", StringManager.removeIllegal("a\"b"));
    }

    @Test
    void removeIllegal_replacesAsterisk() {
        assertEquals("a_b", StringManager.removeIllegal("a*b"));
    }

    @Test
    void removeIllegal_replacesConsecutiveIllegalCharsAsOne() {
        // The pattern uses [...]+ so consecutive illegal chars collapse to one underscore
        assertEquals("a_b", StringManager.removeIllegal("a:\"b"));
    }

    @Test
    void removeIllegal_cleanStringPassesThrough() {
        assertEquals("Hello World", StringManager.removeIllegal("Hello World"));
    }

    // ----------------------------------------------------------------- truncate

    @Test
    void truncate_shortString_passesThrough() {
        String input = "Short string";
        assertEquals(StringManager.removeIllegal(input), StringManager.truncate(input));
    }

    @Test
    void truncate_longString_isCutTo150PlusSuffix() {
        String longInput = "a".repeat(200);
        String result = StringManager.truncate(longInput);
        assertTrue(result.endsWith("(_)"), "Truncated string should end with '(_)'");
        assertEquals(153, result.length(), "Should be 150 chars + 3-char suffix");
    }

    @Test
    void truncate_exactlyAtLimit_isNotTruncated() {
        String input = "a".repeat(150);
        String result = StringManager.truncate(input);
        assertFalse(result.endsWith("(_)"));
        assertEquals(150, result.length());
    }

    // ---------------------------------------------------------- getNullableText

    @Test
    void getNullableText_null_returnsNullString() {
        assertEquals("null", StringManager.getNullableText(null));
    }

    @Test
    void getNullableText_nonNull_returnsOriginal() {
        assertEquals("hello", StringManager.getNullableText("hello"));
    }

    @Test
    void getNullableText_emptyString_returnsEmptyString() {
        assertEquals("", StringManager.getNullableText(""));
    }

    // -------------------------------------------------- humanReadableByteCount

    @Test
    void humanReadableByteCount_belowUnit_returnsBytesLabel() {
        String result = StringManager.humanReadableByteCount(512, false);
        assertTrue(result.contains("512") && result.contains("o"),
                "Sub-kibibyte should show raw value in 'o'");
    }

    @Test
    void humanReadableByteCount_kilobinary_showsKiB() {
        String result = StringManager.humanReadableByteCount(1024, false);
        assertTrue(result.contains("Ki"), "1024 bytes binary should show Ki prefix");
    }

    @Test
    void humanReadableByteCount_kiloSI_showsK() {
        String result = StringManager.humanReadableByteCount(1000, true);
        assertTrue(result.contains("k"), "1000 bytes SI should show k prefix");
    }

    @Test
    void humanReadableByteCount_negativeInput_treatedAsAbsolute() {
        String positive = StringManager.humanReadableByteCount(512, false);
        String negative = StringManager.humanReadableByteCount(-512, false);
        assertEquals(positive, negative, "Negative input should be treated as absolute value");
    }

    // --------------------------------------------------------------- secondsToMMSS

    @Test
    void secondsToMMSS_negative_returnsPlaceholder() {
        assertEquals("--:--", StringManager.secondsToMMSS(-1));
    }

    @Test
    void secondsToMMSS_zero_returnsTwoZeros() {
        assertEquals("00:00", StringManager.secondsToMMSS(0));
    }

    @Test
    void secondsToMMSS_90seconds_returns1Min30() {
        assertEquals("01:30", StringManager.secondsToMMSS(90));
    }

    @Test
    void secondsToMMSS_3600seconds_returns60Min() {
        assertEquals("60:00", StringManager.secondsToMMSS(3600));
    }

    // --------------------------------------------------------------- secondsToHHMM

    @Test
    void secondsToHHMM_negative_returnsPlaceholder() {
        assertEquals("--:--", StringManager.secondsToHHMM(-1));
    }

    @Test
    void secondsToHHMM_zero_returnsTwoZeros() {
        assertEquals("00 h 00", StringManager.secondsToHHMM(0));
    }

    @Test
    void secondsToHHMM_7380seconds_returns2h3() {
        assertEquals("02 h 03", StringManager.secondsToHHMM(7380));
    }

    @Test
    void secondsToHHMM_3600seconds_returns1h0() {
        assertEquals("01 h 00", StringManager.secondsToHHMM(3600));
    }

    // ------------------------------------------------------- humanReadableSeconds

    @Test
    void humanReadableSeconds_zero_returnsDash() {
        assertEquals("-", StringManager.humanReadableSeconds(0));
    }

    @Test
    void humanReadableSeconds_negative_returnsDash() {
        assertEquals("-", StringManager.humanReadableSeconds(-5));
    }

    @Test
    void humanReadableSeconds_seconds_showsSecondsOnly() {
        assertEquals("30s", StringManager.humanReadableSeconds(30));
    }

    @Test
    void humanReadableSeconds_minutesAndSeconds_showsBoth() {
        String result = StringManager.humanReadableSeconds(90); // 1m30s
        assertTrue(result.contains("01m"), "Should contain minutes");
        assertTrue(result.contains("30s"), "Should contain seconds");
    }

    @Test
    void humanReadableSeconds_hoursMinutesSeconds() {
        String result = StringManager.humanReadableSeconds(3690); // 1h01m30s
        assertTrue(result.contains("01h"), "Should contain hours");
        assertTrue(result.contains("01m"), "Should contain minutes");
        assertTrue(result.contains("30s"), "Should contain seconds");
    }

    @Test
    void humanReadableSeconds_days() {
        String result = StringManager.humanReadableSeconds(86400 + 3600); // 1d01h
        assertTrue(result.contains("1d"), "Should contain days");
        assertTrue(result.contains("01h"), "Should contain hours");
    }

    // --------------------------------------------------------------- parseSlashList

    @Test
    void parseSlashList_singleEntry_returnsSingletonList() {
        List<String> result = StringManager.parseSlashList("Rock");
        assertEquals(1, result.size());
        assertEquals("Rock", result.get(0));
    }

    @Test
    void parseSlashList_multipleEntries_splitOnSlashWithSpaces() {
        List<String> result = StringManager.parseSlashList("Rock / Pop / Jazz");
        assertEquals(3, result.size());
        assertEquals("Rock", result.get(0));
        assertEquals("Pop", result.get(1));
        assertEquals("Jazz", result.get(2));
    }

    @Test
    void parseSlashList_slashWithoutSpaces_notSplit() {
        // The separator is " / " (with spaces), so "a/b" is NOT split
        List<String> result = StringManager.parseSlashList("a/b");
        assertEquals(1, result.size());
    }
}
