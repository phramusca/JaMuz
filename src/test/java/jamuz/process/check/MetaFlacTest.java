package jamuz.process.check;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MetaFlacTest {
    @Test
    void shouldInstantiateWithPath() {
        MetaFlac metaFlac = new MetaFlac("/tmp");
        assertNotNull(metaFlac);
    }
}
