package jamuz;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LogFormatTest {

    @Test
    void shouldRenderHtmlHeaderBodyAndTail() {
        LogFormat formatter = new LogFormat();
        LogRecord record = new LogRecord(Level.WARNING, "line1\nline2");
        record.setSourceClassName("ClassA");
        record.setSourceMethodName("methodB");
        String out = formatter.format(record);

        assertTrue(out.contains("#ff8c00"));
        assertTrue(out.contains("WARNING"));
        assertTrue(out.contains("line1<BR/>line2"));
        assertTrue(out.contains("ClassA"));
        assertTrue(out.contains("methodB"));
        assertTrue(formatter.getHead(null).contains("<table"));
        assertTrue(formatter.getTail(null).contains("</html>"));
    }
}
