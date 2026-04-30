package jamuz.process.sync;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeviceTest {

    private static final int ID = 1;

    private Device device() {
        return new Device(ID, "myDevice", "/src/", "/dst/", 2, "host", false);
    }

    @Test
    void constructor_exposesAllFields() {
        Device d = device();
        assertEquals(ID, d.getId());
        assertEquals("myDevice", d.getName());
        assertEquals("/src/", d.getSource());
        assertEquals("/dst/", d.getDestination());
        assertEquals(2, d.getIdPlaylist());
        assertEquals("host", d.getMachineName());
        assertFalse(d.isHidden());
    }

    @Test
    void defaultConstructor_createsNoneDevice() {
        Device d = new Device();
        assertEquals(0, d.getId());
        assertNotNull(d.getName());
    }

    @Test
    void setters_updateFields() {
        Device d = device();
        d.setName("renamed");
        d.setSource("/new-src/");
        d.setDestination("/new-dst/");
        d.setIdPlaylist(7);
        assertEquals("renamed", d.getName());
        assertEquals("/new-src/", d.getSource());
        assertEquals("/new-dst/", d.getDestination());
        assertEquals(7, d.getIdPlaylist());
    }

    @Test
    void equals_withSameFields_returnsTrue() {
        assertEquals(device(), device());
        assertEquals(device().hashCode(), device().hashCode());
    }

    @Test
    void equals_withDifferentId_returnsFalse() {
        Device a = device();
        Device b = new Device(99, "myDevice", "/src/", "/dst/", 2, "host", false);
        assertNotEquals(a, b);
    }

}
