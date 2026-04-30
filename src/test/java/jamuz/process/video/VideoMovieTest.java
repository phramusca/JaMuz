package jamuz.process.video;

import info.movito.themoviedbapi.model.MovieDb;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VideoMovieTest {

    private static MyMovieDb myMovieDb() {
        MovieDb db = new MovieDb();
        db.setId(10);
        db.setTitle("Inception");
        return new MyMovieDb(db);
    }

    @Test
    void isSubclassOfVideoAbstract() {
        assertTrue(VideoAbstract.class.isAssignableFrom(VideoMovie.class));
    }

    @Test
    void constructor_fromMyMovieDb_isMovie() {
        VideoMovie m = new VideoMovie(myMovieDb());
        assertTrue(m.isMovie());
    }

    @Test
    void constructor_fromMyMovieDb_isNotLocalByDefault() {
        VideoMovie m = new VideoMovie(myMovieDb());
        assertFalse(m.isLocal(), "New movie from TMDB has no local file → not local");
    }

    @Test
    void getRelativeFullPath_defaultIsEmpty() {
        VideoMovie m = new VideoMovie(myMovieDb());
        assertNotNull(m.getRelativeFullPath());
    }
}
