package jamuz.process.video;

import info.movito.themoviedbapi.model.tv.TvSeries;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VideoTvShowTest {

    private static MyTvShow myTvShow() {
        TvSeries series = new TvSeries();
        series.setId(5);
        series.setName("Dark");
        return new MyTvShow(series);
    }

    @Test
    void isSubclassOfVideoAbstract() {
        assertTrue(VideoAbstract.class.isAssignableFrom(VideoTvShow.class));
    }

    @Test
    void constructor_fromMyTvShow_isNotMovie() {
        VideoTvShow show = new VideoTvShow(myTvShow());
        assertFalse(show.isMovie());
    }

    @Test
    void constructor_fromMyTvShow_isNotLocalByDefault() {
        VideoTvShow show = new VideoTvShow(myTvShow());
        assertFalse(show.isLocal(), "New TV show from TMDB with no local files → not local");
    }

    @Test
    void getRelativeFullPath_withNoFiles_isEmptyString() {
        VideoTvShow show = new VideoTvShow(myTvShow());
        assertEquals("", show.getRelativeFullPath());
    }
}
