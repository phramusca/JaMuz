package jamuz;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LogFormatTest {

    private final LogFormat formatter = new LogFormat();

    @Test
    void format_withWarning_rendersOrangeColor() {
        String out = format(Level.WARNING, "warn msg");
        assertTrue(out.contains("#ff8c00"), "WARNING should use orange colour");
        assertTrue(out.contains("WARNING"));
        assertTrue(out.contains("warn msg"));
    }

    @Test
    void format_withSevere_rendersRedColor() {
        String out = format(Level.SEVERE, "severe msg");
        assertTrue(out.contains("#ff0000"), "SEVERE should use red colour");
        assertTrue(out.contains("SEVERE"));
    }

    @Test
    void format_withInfo_rendersInfoColor() {
        String out = format(Level.INFO, "info msg");
        assertTrue(out.contains("#ffe7ba"), "INFO should use info colour");
        assertTrue(out.contains("INFO"));
    }

    @Test
    void format_withConfig_rendersConfigColor() {
        assertTrue(format(Level.CONFIG, "cfg").contains("#cdba96"));
    }

    @Test
    void format_withFine_rendersFineColor() {
        assertTrue(format(Level.FINE, "fine").contains("#9fb6cd"));
    }

    @Test
    void format_convertsNewlinesToHtmlBreaks() {
        String out = format(Level.WARNING, "line1\nline2");
        assertTrue(out.contains("line1<BR/>line2"));
    }

    @Test
    void format_includesClassAndMethodName() {
        LogRecord record = new LogRecord(Level.INFO, "msg");
        record.setSourceClassName("ClassA");
        record.setSourceMethodName("methodB");
        String out = formatter.format(record);
        assertTrue(out.contains("ClassA"));
        assertTrue(out.contains("methodB"));
    }

    @Test
    void getHead_containsHtmlTableTag() {
        assertTrue(formatter.getHead(null).contains("<table"));
    }

    @Test
    void getTail_containsHtmlClosingTag() {
        assertTrue(formatter.getTail(null).contains("</html>"));
    }

    private String format(Level level, String message) {
        LogRecord record = new LogRecord(level, message);
        return formatter.format(record);
    }
}
