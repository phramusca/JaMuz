package jamuz;

import jamuz.Playlist.LimitUnit;
import jamuz.Playlist.Match;
import jamuz.Playlist.Type;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlaylistTest {

    private static Playlist playlist(int id, String name) {
        return new Playlist(id, name, false, 0, LimitUnit.files, false,
                Type.Songs, Match.All, false, "mp3");
    }

    @Test
    void constructor_exposesAllFields() {
        Playlist p = playlist(3, "Rock");
        assertEquals(3, p.getId());
        assertEquals("Rock", p.getName());
        assertFalse(p.isLimit());
        assertEquals(0, p.getLimitValue());
        assertEquals(LimitUnit.files, p.getLimitUnit());
        assertFalse(p.isRandom());
        assertEquals(Type.Songs, p.getType());
        assertFalse(p.isHidden());
    }

    @Test
    void setters_updateFields() {
        Playlist p = playlist(1, "original");
        p.setName("renamed");
        p.setLimit(true);
        p.setLimitValue(100);
        p.setLimitUnit(LimitUnit.Mio);
        p.setRandom(true);
        p.setType(Type.Albums);

        assertEquals("renamed", p.getName());
        assertTrue(p.isLimit());
        assertEquals(100, p.getLimitValue());
        assertEquals(LimitUnit.Mio, p.getLimitUnit());
        assertTrue(p.isRandom());
        assertEquals(Type.Albums, p.getType());
    }

    @Test
    void compareTo_sortsByName() {
        Playlist a = playlist(1, "Alpha");
        Playlist b = playlist(2, "Beta");
        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
        assertEquals(0, a.compareTo(playlist(99, "Alpha")));
    }

    @Test
    void limitUnit_hasExpectedValues() {
        assertEquals(5, LimitUnit.values().length);
        assertEquals(LimitUnit.Gio, LimitUnit.valueOf("Gio"));
        assertEquals(LimitUnit.files, LimitUnit.valueOf("files"));
    }

    @Test
    void type_hasExpectedValues() {
        assertEquals(3, Type.values().length);
        assertEquals(Type.Songs, Type.valueOf("Songs"));
    }
}
