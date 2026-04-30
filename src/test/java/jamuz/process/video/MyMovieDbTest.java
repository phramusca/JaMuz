package jamuz.process.video;

import info.movito.themoviedbapi.model.MovieDb;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MyMovieDbTest {

    private static MyMovieDb movie() {
        MovieDb db = new MovieDb();
        db.setId(7);
        db.setTitle("Inception");
        db.setHomepage("https://movie.example.com");
        return new MyMovieDb(db);
    }

    @Test
    void isSubclassOfMyVideoAbstract() {
        assertTrue(MyVideoAbstract.class.isAssignableFrom(MyMovieDb.class));
    }

    @Test
    void getId_returnsMovieDbId() {
        assertEquals(7, movie().getId());
    }

    @Test
    void getHomepage_returnsMovieHomepage() {
        assertEquals("https://movie.example.com", movie().getHomepage());
    }

    @Test
    void getMovieDb_returnsSameInstance() {
        MovieDb db = new MovieDb();
        MyMovieDb m = new MyMovieDb(db);
        assertSame(db, m.getMovieDb());
    }

    @Test
    void setMovieDb_updatesInstance() {
        MyMovieDb m = movie();
        MovieDb newDb = new MovieDb();
        newDb.setId(99);
        m.setMovieDb(newDb);
        assertEquals(99, m.getId());
    }

    @Test
    void toString_containsTitle() {
        assertTrue(movie().toString().contains("Inception"));
    }
}
