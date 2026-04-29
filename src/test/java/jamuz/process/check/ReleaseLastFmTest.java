package jamuz.process.check;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReleaseLastFmTest {
    @Test
    void shouldInstantiateReleaseHelper() {
        assertNotNull(new ReleaseLastFm());
    }
}
