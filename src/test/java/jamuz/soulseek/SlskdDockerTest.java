package jamuz.soulseek;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SlskdDockerTest {

    @Test
    void constants_haveExpectedValues() {
        assertEquals("slskd/slskd", SlskdDocker.DOCKER_IMAGE_REPOSITORY);
        assertFalse(SlskdDocker.DEFAULT_DOCKER_IMAGE_TAG.isBlank());
    }

    @Test
    void buildSharedDirEnvValue_noExclude_returnsMusicContainerPath() {
        String result = SlskdDocker.buildSharedDirEnvValue("/host/music", "");
        assertNotNull(result);
        // Container path is always /music; no exclude segments
        assertFalse(result.isEmpty());
        assertFalse(result.contains("!"), "No exclude prefixes expected without exclusion list");
    }

    @Test
    void buildSharedDirEnvValue_withExclude_addsExcludeSegments() {
        String result = SlskdDocker.buildSharedDirEnvValue("/host/music", "lossy");
        assertTrue(result.contains("!"), "Should contain exclude segment prefixed with '!'");
    }
}
