package jamuz.process.merge;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StatSourceTest {
    @Test
    void shouldInstantiateWithMachineName() {
        StatSource source = new StatSource("machine");
        assertNotNull(source);
        assertEquals("machine", source.getMachineName());
    }
}
