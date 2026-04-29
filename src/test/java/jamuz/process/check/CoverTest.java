package jamuz.process.check;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CoverTest {
    @Test
    void shouldExposeCoverTypeEnum() {
        assertTrue(Cover.CoverType.values().length > 0);
    }
}
