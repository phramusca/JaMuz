package jamuz.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProcessAbstractTest {

    static class ConcreteProcess extends ProcessAbstract {
        ConcreteProcess() { super("test-process"); }
    }

    @Test
    void abort_thenCheckAbort_throwsInterruptedException() throws Exception {
        ConcreteProcess p = new ConcreteProcess();
        p.abort();
        assertThrows(InterruptedException.class, p::checkAbort);
    }

    @Test
    void resetAbort_afterAbort_allowsCheckAbortWithoutException() throws Exception {
        ConcreteProcess p = new ConcreteProcess();
        p.abort();
        p.resetAbort();
        assertDoesNotThrow(p::checkAbort);
    }
}
