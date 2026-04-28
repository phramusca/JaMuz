package jamuz.process.check;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CoverMBTest {

    @Test
    void shouldExposeInheritedCoverProperties() {
        CoverMB cover = new CoverMB(Cover.CoverType.FILE, "/tmp/a.png", "Name");

        assertEquals(Cover.CoverType.FILE, cover.getType());
        assertEquals("/tmp/a.png", cover.getValue());
        assertEquals("Name", cover.getName());
    }
}
