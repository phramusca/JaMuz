package jamuz.process.video;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FileInfoVideoTest {

    // Helper: build a FileInfoVideo with a single video stream of given width × height
    private static FileInfoVideo withVideoStream(int width, int height, int durationSecs,
                                                  int season, int episode) {
        FileInfoVideo.StreamDetails sd = new FileInfoVideo.StreamDetails();
        sd.addVideoStream("h264", 1.78, width, height, durationSecs);
        return new FileInfoVideo(
                1, 1, "path/to/file.mkv",
                3, "1970-01-01 00:00:00", "1970-01-01 00:00:00", 0,
                sd, season, episode, "Some Title");
    }

    // Helper: build a FileInfoVideo with no video stream
    private static FileInfoVideo withNoVideoStream() {
        FileInfoVideo.StreamDetails sd = new FileInfoVideo.StreamDetails();
        return new FileInfoVideo(
                1, 1, "path/to/file.mkv",
                3, "1970-01-01 00:00:00", "1970-01-01 00:00:00", 0,
                sd, 1, 1, "Some Title");
    }

    // ----------------------------------------------------------------- isHD

    @Test
    void isHD_1080Resolution_returnsTrue() {
        FileInfoVideo f = withVideoStream(1920, 1080, 3600, 1, 1);
        assertTrue(f.isHD());
    }

    @Test
    void isHD_720Resolution_returnsTrue() {
        FileInfoVideo f = withVideoStream(1280, 720, 3600, 1, 1);
        assertTrue(f.isHD());
    }

    @Test
    void isHD_sdResolution_returnsFalse() {
        FileInfoVideo f = withVideoStream(720, 480, 3600, 1, 1);
        assertFalse(f.isHD());
    }

    @Test
    void isHD_noVideoStream_returnsFalse() {
        assertFalse(withNoVideoStream().isHD());
    }

    // --------------------------------------------------- getVideoStreamDetails

    @Test
    void getVideoStreamDetails_1080WithDuration_containsQualityAndTime() {
        FileInfoVideo f = withVideoStream(1920, 1080, 7380, 1, 1); // 2h03
        String details = f.getVideoStreamDetails();
        assertTrue(details.contains("HD 1080"), "Should mention HD 1080");
        assertTrue(details.contains("02 h 03"), "Should include formatted duration");
    }

    @Test
    void getVideoStreamDetails_noVideoStream_returnsEmpty() {
        assertEquals("", withNoVideoStream().getVideoStreamDetails());
    }

    // ----------------------------------------------- getFormattedEpisodeNumber

    @Test
    void getFormattedEpisodeNumber_zeroPadsSeasonAndEpisode() {
        FileInfoVideo f = withVideoStream(1280, 720, 3600, 3, 7);
        assertEquals("S03E07", f.getFormattedEpisodeNumber());
    }

    @Test
    void getFormattedEpisodeNumber_doubledigitValues() {
        FileInfoVideo f = withVideoStream(1280, 720, 3600, 12, 24);
        assertEquals("S12E24", f.getFormattedEpisodeNumber());
    }

    // ------------------------------------------- audio / subtitles formatting

    @Test
    void audioStreamDetails_withLanguage_containsUppercaseLanguage() {
        FileInfoVideo.StreamDetails sd = new FileInfoVideo.StreamDetails();
        sd.addVideoStream("h264", 1.78, 1280, 720, 3600);
        sd.addAudioStream("ac3", 6, "fre");
        FileInfoVideo f = new FileInfoVideo(1, 1, "x.mkv", 3,
                "1970-01-01 00:00:00", "1970-01-01 00:00:00", 0,
                sd, 1, 1, "T");
        assertTrue(f.getAudioStreamDetails().contains("FRE"));
    }

    @Test
    void subtitlesStreamDetails_withLanguage_containsUppercaseLanguage() {
        FileInfoVideo.StreamDetails sd = new FileInfoVideo.StreamDetails();
        sd.addVideoStream("h264", 1.78, 1280, 720, 3600);
        sd.addSubtitle("eng");
        FileInfoVideo f = new FileInfoVideo(1, 1, "x.mkv", 3,
                "1970-01-01 00:00:00", "1970-01-01 00:00:00", 0,
                sd, 1, 1, "T");
        assertTrue(f.getSubtitlesStreamDetails().contains("ENG"));
    }

    @Test
    void subtitlesStreamDetails_blankLanguage_isSkipped() {
        FileInfoVideo.StreamDetails sd = new FileInfoVideo.StreamDetails();
        sd.addVideoStream("h264", 1.78, 1280, 720, 3600);
        sd.addSubtitle("   ");
        FileInfoVideo f = new FileInfoVideo(1, 1, "x.mkv", 3,
                "1970-01-01 00:00:00", "1970-01-01 00:00:00", 0,
                sd, 1, 1, "T");
        assertEquals("", f.getSubtitlesStreamDetails());
    }

    // ----------------------------------------------- default (no-arg) ctor

    @Test
    void defaultConstructor_instantiatesWithoutError() {
        assertNotNull(new FileInfoVideo());
    }
}
