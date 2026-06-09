package jamuz.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DependenciesTest {
    @Test
    void shouldExposeDockerCheckMethod() {
        assertDoesNotThrow(() -> Dependencies.checkDocker(null));
    }
}
