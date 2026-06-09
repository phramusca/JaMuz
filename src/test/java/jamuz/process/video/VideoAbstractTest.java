package jamuz.process.video;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VideoAbstractTest {

    @Test
    void shouldDefineNestedStatusType() {
        assertNotNull(VideoAbstract.Status.class);
    }

    // ------------------------------------------------- VideoAbstract.Status

    @Test
    void status_initialState_isOkWithEmptyMessage() {
        VideoAbstract.Status s = new VideoAbstract.Status();
        assertEquals("", s.getMsg(), "Initial message should be empty");
    }

    @Test
    void status_set_appendsMessageAndMarksAsNotOK() {
        VideoAbstract.Status s = new VideoAbstract.Status();
        s.set("something went wrong");
        assertTrue(s.getMsg().contains("something went wrong"),
                "Message should contain the set status text");
    }

    @Test
    void status_set_appendsSeparatorBetweenMultipleMessages() {
        VideoAbstract.Status s = new VideoAbstract.Status();
        s.set("error1");
        s.set("error2");
        String msg = s.getMsg();
        assertTrue(msg.contains("error1"), "First message should be present");
        assertTrue(msg.contains("error2"), "Second message should be present");
        assertTrue(msg.contains("|"), "Messages should be separated by '|'");
    }

    @Test
    void status_set_multipleTimes_accumulatesAllMessages() {
        VideoAbstract.Status s = new VideoAbstract.Status();
        s.set("A");
        s.set("B");
        s.set("C");
        String msg = s.getMsg();
        assertTrue(msg.contains("A") && msg.contains("B") && msg.contains("C"),
                "All messages should be accumulated");
    }
}
