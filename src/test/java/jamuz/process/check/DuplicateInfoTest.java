package jamuz.process.check;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DuplicateInfoTest {
    @Test
    void shouldExposeMutableFieldsAndToString() {
        DuplicateInfo info = new DuplicateInfo("alb", "art", 4.0, FolderInfo.CheckedFlag.OK, 0, 1, 1, null);
        assertEquals("alb", info.getAlbum());
        info.setAlbum("alb2");
        info.setAlbumArtist("art2");
        info.setRating(3.0);
        info.setDiscNo(2);
        info.setDiscTotal(3);
        assertEquals("alb2", info.getAlbum());
        assertNotNull(info.toString());
    }
}
