package jamuz.soulseek;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ICallBackSearchTest {

    @Test
    void shouldDefineSingleSearchingCallbackMethod() {
        assertTrue(ICallBackSearch.class.isInterface());
        Method[] methods = ICallBackSearch.class.getDeclaredMethods();
        assertEquals(1, methods.length);
        assertEquals("searching", methods[0].getName());
    }
}
