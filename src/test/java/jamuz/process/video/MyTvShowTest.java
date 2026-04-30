package jamuz.process.video;

import info.movito.themoviedbapi.model.tv.TvSeries;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MyTvShowTest {

    private static MyTvShow show() {
        TvSeries series = new TvSeries();
        series.setId(42);
        series.setName("Breaking Bad");
        series.setHomepage("https://example.com");
        return new MyTvShow(series);
    }

    @Test
    void isSubclassOfMyVideoAbstract() {
        assertTrue(MyVideoAbstract.class.isAssignableFrom(MyTvShow.class));
    }

    @Test
    void getId_returnsSeriesId() {
        assertEquals(42, show().getId());
    }

    @Test
    void getHomepage_returnsSeriesHomepage() {
        assertEquals("https://example.com", show().getHomepage());
    }

    @Test
    void getSerie_returnsSameInstance() {
        TvSeries series = new TvSeries();
        MyTvShow s = new MyTvShow(series);
        assertSame(series, s.getSerie());
    }

    @Test
    void setSerie_updatesSerie() {
        MyTvShow s = show();
        TvSeries replacement = new TvSeries();
        replacement.setId(99);
        s.setSerie(replacement);
        assertEquals(99, s.getId());
    }

    @Test
    void toString_containsSeriesName() {
        assertTrue(show().toString().contains("Breaking Bad"),
                "toString should contain the series name");
    }
}
