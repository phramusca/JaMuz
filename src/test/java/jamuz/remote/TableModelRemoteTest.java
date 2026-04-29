package jamuz.remote;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TableModelRemoteTest {
    @Test
    void shouldAddAndRemoveClients() {
        TableModelRemote model = new TableModelRemote();
        model.setColumnNames();
        ClientInfo c = new ClientInfo("u", "p", "/", "name", true);
        model.add(c);
        assertEquals(1, model.getRowCount());
        assertTrue(model.contains("u"));
        model.removeClient("u");
        assertEquals(0, model.getRowCount());
    }
}
