package jamuz.gui.swing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordFieldWithToggleTest {

    @Test
    void shouldStoreAndExposeTextAndPassword() {
        PasswordFieldWithToggle field = new PasswordFieldWithToggle();
        field.setPassword("abc".toCharArray());

        assertEquals("abc", field.getText());
        assertArrayEquals("abc".toCharArray(), field.getPassword());

        field.setEnabled(false);
        assertFalse(field.isEnabled());
    }
}
