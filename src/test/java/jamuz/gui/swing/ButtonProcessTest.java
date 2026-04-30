package jamuz.gui.swing;

import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ButtonProcessTest {

    @Test
    void shouldInitializeAsStoppedForIconAndTextConstructors() {
        ButtonProcess withText = new ButtonProcess("Run");
        ButtonProcess withIcon = new ButtonProcess(new ImageIcon(new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB)));

        assertFalse(withText.isRunning());
        assertFalse(withIcon.isRunning());
    }
}
