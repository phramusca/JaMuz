package jamuz.process.book;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProcessBookTest {
    @Test
    void shouldInstantiateWithName() {
        ProcessBook process = new ProcessBook("p");
        assertNotNull(process);
    }
}
