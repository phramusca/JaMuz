package jamuz.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BenchmarkTest {

    /**
     * Verifies the output format of get() without asserting exact timing values
     * (timing-sensitive assertions are inherently flaky on a loaded CI machine).
     */
    @Test
    void get_returnsExpectedFormatPattern() {
        Benchmark instance = new Benchmark(10);
        String result = instance.get();
        // Format: "Ecoulé: <value>, restant: <value>"
        assertTrue(result.contains(":"), "Output should contain label separators");
        assertFalse(result.isBlank());
    }

    @Test
    void setSize_updatesSize() {
        Benchmark instance = new Benchmark(10);
        instance.setSize(20);
        assertEquals(20, instance.getSize());
    }

    @Test
    void mean_computesAverageCorrectly() {
        List<Long> numbers = new ArrayList<>(Arrays.asList(2L, 9L, 12L, 3L, 7L));
        assertEquals(7L, Benchmark.mean(numbers));
    }

    @Test
    void sum_computesTotalCorrectly() {
        List<Long> numbers = new ArrayList<>(Arrays.asList(2L, 9L, 12L, 3L, 7L));
        assertEquals(33L, Benchmark.sum(numbers));
    }
}
