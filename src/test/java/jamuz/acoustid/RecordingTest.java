package jamuz.acoustid;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RecordingTest {

    @Test
    void shouldExposeRecordingId() {
        Recording recording = new Recording("rec-1");
        assertEquals("rec-1", recording.getId());
    }
}
