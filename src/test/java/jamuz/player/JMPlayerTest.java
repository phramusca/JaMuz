package jamuz.player;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JMPlayerTest {

    @Test
    void defaultConstructor_createInstance() {
        JMPlayer player = new JMPlayer();
        assertNotNull(player);
    }

    @Test
    void getMPlayerPath_defaultIsNotNull() {
        JMPlayer player = new JMPlayer();
        assertNotNull(player.getMPlayerPath());
    }

    @Test
    void setMPlayerPath_updatesPath() {
        JMPlayer player = new JMPlayer();
        player.setMPlayerPath("/usr/bin/mplayer");
        assertEquals("/usr/bin/mplayer", player.getMPlayerPath());
    }

    @Test
    void isPlaying_defaultIsFalse() {
        JMPlayer player = new JMPlayer();
        assertFalse(player.isPlaying());
    }

    @Test
    void getPlayingFile_defaultIsNull() {
        JMPlayer player = new JMPlayer();
        assertNull(player.getPlayingFile());
    }
}
