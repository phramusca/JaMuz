package jamuz.process.sync;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeviceTest {
    @Test
    void shouldExposeBasicFieldsAndEquality() {
        Device a = new Device(1, "d", "s", "dst", 2, "m", false);
        Device b = new Device(1, "d", "s", "dst", 2, "m", false);
        assertEquals(1, a.getId());
        assertEquals("d", a.getName());
        assertEquals("s", a.getSource());
        assertEquals("dst", a.getDestination());
        assertEquals(a, b);
    }
}
