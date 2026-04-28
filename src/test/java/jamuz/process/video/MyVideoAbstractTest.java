package jamuz.process.video;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MyVideoAbstractTest {

    private static final class DummyMyVideo extends MyVideoAbstract {
        int cacheWrites;
        @Override public int getId() { return 1; }
        @Override public void setMyVideoInCache() { cacheWrites++; }
        @Override public String getHomepage() { return "http://example"; }
    }

    @Test
    void shouldUpdateFlagsAndRatingsAndParseYear() {
        DummyMyVideo video = new DummyMyVideo();
        assertFalse(video.isIsFavorite());
        assertFalse(video.isIsInWatchList());

        video.setIsFavorite(true);
        video.setIsInWatchList(true);
        video.setUserRating(new VideoRating(3, "***"));

        assertTrue(video.isIsFavorite());
        assertTrue(video.isIsInWatchList());
        assertEquals(3, video.getUserRating().getRating());
        assertEquals(3, video.cacheWrites);
        assertEquals(2024, MyVideoAbstract.getYear("2024-02-01"));
        assertEquals(0, MyVideoAbstract.getYear("invalid"));
    }
}
