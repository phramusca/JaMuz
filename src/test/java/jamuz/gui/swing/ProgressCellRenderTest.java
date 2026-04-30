package jamuz.gui.swing;

import javax.swing.JTable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProgressCellRenderTest {

    @Test
    void shouldReturnGivenProgressBarInstance() {
        ProgressCellRender render = new ProgressCellRender();
        ProgressBar bar = new ProgressBar();
        bar.setup(100);

        assertSame(bar, render.getTableCellRendererComponent(new JTable(), bar, false, false, 0, 0));
    }
}
