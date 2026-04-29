package jamuz.process.sync;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProcessSyncTest {
    @Test
    void shouldInstantiateWithRequiredDependencies() {
        ProcessSync sync = new ProcessSync("sync", new Device("machine"), new jamuz.gui.swing.ProgressBar(), new ICallBackSync() {
            @Override
            public void refresh() { }
            @Override
            public void enable() { }
            @Override
            public void enableButton(boolean enable) { }
            @Override
            public void addRow(String file, int idIcon) { }
            @Override
            public void addRow(String file, String msg) { }
        });
        assertNotNull(sync);
    }
}
