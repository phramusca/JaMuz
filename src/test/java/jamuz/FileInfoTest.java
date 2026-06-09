package jamuz;

import java.util.Date;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FileInfoTest {

    // ---- original getter/setter/equals/clone tests (converted to JUnit5) ----

    @Test
    void getIdFile_afterSet_returnsSetValue() {
        FileInfo f = new FileInfo("source", "path");
        f.setIdFile(1);
        assertEquals(1, f.getIdFile());
    }

    @Test
    void setIdFile_updatesField() {
        FileInfo f = new FileInfo("source", "path");
        f.setIdFile(2);
        assertEquals(2, f.getIdFile());
    }

    @Test
    void setAndGetLastPlayed_roundTrip() {
        FileInfo f = new FileInfo("source", "path");
        Date now = new Date();
        f.setLastPlayed(now);
        assertEquals(now, f.getLastPlayed());
    }

    @Test
    void equals_sameRelativeFullPath_isEqual() {
        FileInfo f1 = new FileInfo("source", "path1");
        FileInfo f2 = new FileInfo("source", "path1");
        assertEquals(f1, f2);
    }

    @Test
    void equals_differentRelativeFullPath_notEqual() {
        FileInfo f1 = new FileInfo("source", "path1");
        FileInfo f2 = new FileInfo("source", "path2");
        assertNotEquals(f1, f2);
    }

    @Test
    void hashCode_samePath_equal() {
        FileInfo f1 = new FileInfo("source", "path1");
        FileInfo f2 = new FileInfo("source", "path1");
        assertEquals(f1.hashCode(), f2.hashCode());
    }

    @Test
    void clone_producesDifferentInstanceWithEqualContent() throws CloneNotSupportedException {
        FileInfo f1 = new FileInfo("source", "path1");
        FileInfo f2 = (FileInfo) f1.clone();
        assertEquals(f1, f2);
        assertNotSame(f1, f2);
    }

    // ---- path decomposition ----

    @Test
    void setPath_decomposesRelativeFullPathIntoComponents() {
        FileInfo f = new FileInfo("src", "artist/album/song.mp3");
        assertEquals("artist/album/song.mp3", f.getRelativeFullPath());
        assertTrue(f.getRelativePath().contains("artist") && f.getRelativePath().contains("album"),
                "relativePath should contain parent directories");
        assertEquals("song.mp3", f.getFilename());
        assertEquals("mp3", f.getExt());
    }

    @Test
    void setPath_extensionIsLowercased() {
        FileInfo f = new FileInfo("src", "song.MP3");
        assertEquals("mp3", f.getExt());
    }

    @Test
    void setPath_noExtension_extIsEmpty() {
        FileInfo f = new FileInfo("src", "a/b/noext");
        assertEquals("", f.getExt());
    }

    @Test
    void setExt_changesExtensionInPath() {
        FileInfo f = new FileInfo("src", "artist/album/song.mp3");
        f.setExt("flac");
        assertTrue(f.getFilename().endsWith(".flac"), "Filename should end with .flac");
        assertFalse(f.getFilename().endsWith(".mp3"), "Filename should no longer end with .mp3");
    }

    @Test
    void setFilename_updatesFilenameAndRebuildRelativeFullPath() {
        FileInfo f = new FileInfo("src", "artist/album/song.mp3");
        f.setFilename("new-song.flac");
        assertEquals("new-song.flac", f.getFilename());
        assertTrue(f.getRelativeFullPath().endsWith("new-song.flac"),
                "relativeFullPath should end with new filename");
    }

    // ---- compareTo ----

    @Test
    void compareTo_sortsAlphabeticallyByRelativeFullPath() {
        FileInfo earlier = new FileInfo("src", "artist/album/a.mp3");
        FileInfo later   = new FileInfo("src", "artist/album/z.mp3");
        assertTrue(earlier.compareTo(later) < 0, "'a.mp3' should sort before 'z.mp3'");
        assertTrue(later.compareTo(earlier) > 0, "'z.mp3' should sort after 'a.mp3'");
        assertEquals(0, earlier.compareTo(new FileInfo("src", "artist/album/a.mp3")));
    }

    // ---- equalsStats ----

    @Test
    void equalsStats_sameDefaultStats_returnsTrue() {
        FileInfo a = new FileInfo("src", "path/song.mp3");
        FileInfo b = new FileInfo("src", "path/song.mp3");
        assertTrue(a.equalsStats(b), "Two freshly-constructed FileInfos should have equal stats");
    }

    @Test
    void equalsStats_differentRating_returnsFalse() {
        FileInfo a = new FileInfo("src", "path/song.mp3");
        FileInfo b = new FileInfo("src", "path/song.mp3");
        a.setRating(3);
        assertFalse(a.equalsStats(b), "Different rating should make equalsStats false");
    }

    @Test
    void equalsStats_differentPlayCounter_returnsFalse() {
        FileInfo a = new FileInfo("src", "path/song.mp3");
        FileInfo b = new FileInfo("src", "path/song.mp3");
        a.setPlayCounter(5);
        assertFalse(a.equalsStats(b), "Different play counter should make equalsStats false");
    }

    @Test
    void equalsStats_differentGenre_returnsFalse() {
        FileInfo a = new FileInfo("src", "path/song.mp3");
        FileInfo b = new FileInfo("src", "path/song.mp3");
        a.setGenre("Rock");
        assertFalse(a.equalsStats(b), "Different genre should make equalsStats false");
    }
}
