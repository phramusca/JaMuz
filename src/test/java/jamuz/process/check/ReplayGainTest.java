package jamuz.process.check;

import jamuz.process.check.ReplayGain.GainValues;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReplayGainTest {

    @Test
    void gainValues_defaultConstructor_isNotValid() {
        GainValues gv = new GainValues();
        assertFalse(gv.isValid(), "Default GainValues with NaN should not be valid");
    }

    @Test
    void gainValues_parameterizedConstructor_isValid() {
        GainValues gv = new GainValues(12.1f, 53.6f);
        assertTrue(gv.isValid());
        assertEquals(12.1f, gv.getTrackGain(), 0.001f);
        assertEquals(53.6f, gv.getAlbumGain(), 0.001f);
    }

    @Test
    void gainValues_toString_containsAlbumAndTrackGain() {
        GainValues gv = new GainValues(1.0f, 2.0f);
        String s = gv.toString();
        assertTrue(s.contains("albumGain"));
        assertTrue(s.contains("trackGain"));
    }

    @Test
    void gainValues_equals_withSameValues_returnsTrue() {
        GainValues a = new GainValues(1.0f, 2.0f);
        GainValues b = new GainValues(1.0f, 2.0f);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void gainValues_equals_withDifferentValues_returnsFalse() {
        assertNotEquals(new GainValues(1.0f, 2.0f), new GainValues(3.0f, 4.0f));
    }

    @Test
    @SuppressWarnings("unchecked")
    void gainValues_toMap_containsAlbumAndTrackGainKeys() {
        GainValues gv = new GainValues(1.5f, 2.5f);
        Map<String, Object> map = gv.toMap();
        assertTrue(map.containsKey("albumGain"));
        assertTrue(map.containsKey("trackGain"));
    }
}
