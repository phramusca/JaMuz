package jamuz;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MachineTest {

    private Machine machine;
    private ArrayList<Option> options;

    @BeforeEach
    void setUp() {
        machine = new Machine("test-host");
        options = new ArrayList<>();
        options.add(new Option("location.library", "/music/", 1, 1, "path"));
        options.add(new Option("library.isMaster", "true", 1, 2, "bool"));
        machine.setOptions(options);
    }

    @Test
    void getName_returnsConstructorValue() {
        assertEquals("test-host", machine.getName());
    }

    @Test
    void getDescription_isEmptyAfterConstruction() {
        assertEquals("", machine.getDescription());
    }

    @Test
    void getOptions_returnsSetList() {
        assertSame(options, machine.getOptions());
    }

    @Test
    void getOption_byIndex_returnsCorrectOption() {
        assertEquals("location.library", machine.getOption(0).getId());
        assertEquals("library.isMaster", machine.getOption(1).getId());
    }

    @Test
    void getOption_byId_returnsMatchingOption() {
        Option opt = machine.getOption("location.library");
        assertNotNull(opt);
        assertEquals("/music/", opt.getValue());
    }

    @Test
    void getOption_byId_withUnknownId_returnsNull() {
        assertNull(machine.getOption("no.such.option"));
    }

    @Test
    void getOptionValue_returnsValueForKnownOption() {
        assertEquals("true", machine.getOptionValue("library.isMaster"));
    }
}
