package jamuz.process.check;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReleaseLastFmTest {

    @Test
    void defaultConstructor_createsInstance() {
        assertNotNull(new ReleaseLastFm());
    }

    @Test
    void getCoverList_beforeSearch_isNull() {
        ReleaseLastFm r = new ReleaseLastFm();
        assertNull(r.getCoverList(),
                "getCoverList should return null before any search is performed");
    }
}
