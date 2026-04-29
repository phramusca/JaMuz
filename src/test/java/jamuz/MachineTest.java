package jamuz;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MachineTest {
    @Test
    void shouldExposeNameAndOptionsContainer() {
        Machine machine = new Machine("m1");
        assertEquals("m1", machine.getName());
        machine.setOptions(new ArrayList<>());
        assertNotNull(machine.getOptions());
    }
}
