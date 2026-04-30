package jamuz.remote;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClientInfoTest {

    private ClientInfo newInfo(String login) {
        return new ClientInfo(login, "pwd", "/root", "name", true);
    }

    @Test
    void constructor_exposesLoginPwdNameAndEnabled() {
        ClientInfo info = newInfo("login");
        assertEquals("login", info.getLogin());
        assertEquals("pwd", info.getPwd());
        assertEquals("name", info.getName());
        assertTrue(info.isEnabled());
    }

    @Test
    void progressBar_isNotNull() {
        assertNotNull(newInfo("l").getProgressBar());
    }

    @Test
    void isConnected_isFalseByDefault() {
        assertFalse(newInfo("l").isConnected());
    }

    @Test
    void setConnected_updatesConnectedState() {
        ClientInfo info = newInfo("l");
        info.setConnected(true);
        assertTrue(info.isConnected());
        info.setConnected(false);
        assertFalse(info.isConnected());
    }

    @Test
    void setStatus_prependsTimestampAndContainsOriginalMessage() {
        ClientInfo info = newInfo("l");
        info.setStatus("playing");
        assertTrue(info.getStatus().contains("playing"),
                "Status should contain the set message");
    }

    @Test
    void status_isEmptyByDefault() {
        assertEquals("", newInfo("l").getStatus());
    }

    @Test
    void equals_withSameFields_returnsTrue() {
        ClientInfo a = newInfo("login");
        ClientInfo b = newInfo("login");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void equals_withDifferentLogin_returnsFalse() {
        assertNotEquals(newInfo("alice"), newInfo("bob"));
    }
}
