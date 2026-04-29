package jamuz.process.video;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MyTvShowTest {
    @Test
    void shouldInheritFromMyVideoAbstract() {
        assertTrue(MyVideoAbstract.class.isAssignableFrom(MyTvShow.class));
    }
}
