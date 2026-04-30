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
    void defaults_areFalseAndNotRated() {
        DummyMyVideo video = new DummyMyVideo();
        assertFalse(video.isIsFavorite());
        assertFalse(video.isIsInWatchList());
        assertNotNull(video.getUserRating());
        assertEquals(0, video.getUserRating().getRating());
        assertEquals("Not Rated", video.getUserRating().toString());
    }

    @Test
    void setIsFavorite_updatesAndCallsCache() {
        DummyMyVideo video = new DummyMyVideo();
        video.setIsFavorite(true);
        assertTrue(video.isIsFavorite());
        assertEquals(1, video.cacheWrites);
    }

    @Test
    void setIsInWatchList_updatesAndCallsCache() {
        DummyMyVideo video = new DummyMyVideo();
        video.setIsInWatchList(true);
        assertTrue(video.isIsInWatchList());
        assertEquals(1, video.cacheWrites);
    }

    @Test
    void setUserRating_updatesAndCallsCache() {
        DummyMyVideo video = new DummyMyVideo();
        VideoRating rating = new VideoRating(3, "***");
        video.setUserRating(rating);
        assertEquals(3, video.getUserRating().getRating());
        assertEquals(1, video.cacheWrites);
    }

    @Test
    void getYear_withIsoDate_returnsYear() {
        assertEquals(2024, MyVideoAbstract.getYear("2024-02-01"));
    }

    @Test
    void getYear_withInvalidDate_returnsZero() {
        assertEquals(0, MyVideoAbstract.getYear("invalid"));
        assertEquals(0, MyVideoAbstract.getYear(""));
    }

    @Test
    void getYear_withNull_throwsException() {
        // SimpleDateFormat.parse(null) throws NullPointerException, caught as ParseException may vary
        assertThrows(Exception.class, () -> MyVideoAbstract.getYear(null));
    }
}
