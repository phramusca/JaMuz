package jamuz.process.video;

import jamuz.gui.swing.TriStateCheckBox;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TableRowFilterVideoTest {
    @Test
    void shouldAllowChangingFilterCriteria() {
        TableRowFilterVideo filter = new TableRowFilterVideo();
        filter.displaySelected(TriStateCheckBox.State.ALL);
        filter.displayMovies(TriStateCheckBox.State.SELECTED);
        filter.displayByRating("all");
        assertNotNull(filter);
    }
}
