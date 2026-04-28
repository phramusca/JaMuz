package jamuz.acoustid;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResultsTest {

    @Test
    void shouldReturnNullWhenStatusNotOkOrNoResults() {
        Results results = new Results();
        results.status = "error";
        results.results = new ArrayList<>();
        assertNull(results.getBest());
    }

    @Test
    void shouldExposeChromaprint() {
        Results results = new Results();
        results.chromaprint = new ChromaPrint("c", "10");
        assertEquals("c", results.getChromaprint().getChromaprint());
    }
}
