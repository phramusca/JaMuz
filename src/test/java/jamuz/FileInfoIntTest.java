package jamuz;

import jamuz.process.check.FolderInfo;
import jamuz.process.sync.SyncStatus;
import jamuz.process.check.FolderInfo.CheckedFlag;
import jamuz.process.check.ReplayGain.GainValues;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class FileInfoIntTest {

    private FileInfoInt fileInfoInt;
    private FileInfoInt fileInfoIntFromDb;

    @BeforeEach
    void setUp() {
        fileInfoInt = new FileInfoInt("test/path", "root/path");
        
//        FileInfoInt(int idFile, int idPath, String relativePath,
//			String filename, int length, String format, String bitRate,
//			int size, float BPM, String album, String albumArtist,
//			String artist, String comment, int discNo, int discTotal,
//			String genre, int nbCovers, String title, int trackNo,
//			int trackTotal, String year, int playCounter, int rating,
//			String addedDate, String lastPlayed, String modifDate, 
//			String coverHash, CheckedFlag checkedFlag,
//			FolderInfo.CopyRight copyRight, double albumRating,
//			int percentRated, String rootPath, SyncStatus status,
//			String pathModifDate, String pathMbid, GainValues replaygain)
        fileInfoIntFromDb = new FileInfoInt(512, 24, "path/to/", "file.mp3", 
                5154, "format inconnu", "84.544", 1598798, 62.15f, "test album", "test album artist", 
                "test artist", "test comment", 2, 14, "test genre", 84, "test title", 4, 9, "test year", 15, 12, 
                "test added date", "test last played", "test modif date", 
                "test cover hash", CheckedFlag.UNCHECKED, 
                FolderInfo.CopyRight.CONTRIBUTED, 65, 84, "test toot path", SyncStatus.NEW, "test path modif date", "test path mbid", new GainValues(12.1f, 53.6f));
    }
    
    @Test
    void testIdFile() {
        assertEquals(512, fileInfoInt.getIdFile());
        fileInfoInt.setIdFile(665);
        assertEquals(665, fileInfoInt.getIdFile());
    }
    
    @Test
    void testIdPath() {
        assertEquals(24, fileInfoInt.getIdPath());
        fileInfoInt.setIdPath(217);
        assertEquals(217, fileInfoInt.getIdPath());
    }

    @Test
    void testGetTrackNo() {
        assertEquals(-1, fileInfoInt.getTrackNo());
        fileInfoInt.trackNo = 5;
        assertEquals(5, fileInfoInt.getTrackNo());
    }

    @Test
    void testGetTrackTotal() {
        assertEquals(-1, fileInfoInt.getTrackTotal());
        fileInfoInt.trackTotal = 10;
        assertEquals(10, fileInfoInt.getTrackTotal());
    }

    @Test
    void testGetDiscNo() {
        assertEquals(-1, fileInfoInt.getDiscNo());
        fileInfoInt.discNo = 1;
        assertEquals(1, fileInfoInt.getDiscNo());
    }

    @Test
    void testGetDiscTotal() {
        assertEquals(-1, fileInfoInt.getDiscTotal());
        fileInfoInt.discTotal = 2;
        assertEquals(2, fileInfoInt.getDiscTotal());
    }

    @Test
    void testGetComment() {
        assertEquals("", fileInfoInt.getComment());
        fileInfoInt.comment = "Test comment";
        assertEquals("Test comment", fileInfoInt.getComment());
    }

    @Test
    void testGetNbCovers() {
        assertEquals(0, fileInfoInt.getNbCovers());
        fileInfoInt.nbCovers = 3;
        assertEquals(3, fileInfoInt.getNbCovers());
    }

    @Test
    void testGetCheckedFlag() {
        assertEquals(CheckedFlag.UNCHECKED, fileInfoInt.getCheckedFlag());
        assertEquals(CheckedFlag.OK, fileInfoIntFromDb.getCheckedFlag());
    }

    @Test
    void testGetBitRate() {
        assertEquals("", fileInfoInt.getBitRate());
        assertEquals("320kbps", fileInfoIntFromDb.getBitRate());
    }

    @Test
    void testGetFormat() {
        assertEquals("", fileInfoInt.getFormat());
        assertEquals("mp3", fileInfoIntFromDb.getFormat());
    }

    @Test
    void testGetLength() {
        assertEquals(0, fileInfoInt.getLength());
        fileInfoInt.length = 300;
        assertEquals(300, fileInfoInt.getLength());
    }

    @Test
    void testGetSize() {
        assertEquals(0, fileInfoInt.getSize());
        fileInfoInt.size = 123456L;
        assertEquals(123456L, fileInfoInt.getSize());
    }

    @Test
    void testGetStatus() {
        assertEquals(SyncStatus.NEW, fileInfoInt.getStatus());
        fileInfoInt.setStatus(SyncStatus.INFO);
        assertEquals(SyncStatus.INFO, fileInfoInt.getStatus());
    }

    @Test
    void testSetStatus() {
        fileInfoInt.setStatus(SyncStatus.NEW);
        assertEquals(SyncStatus.NEW, fileInfoInt.getStatus());
    }

    @Test
    void testGetFullPath() {
        File expectedFile = new File("root/path/test/path");
        assertEquals(expectedFile, fileInfoInt.getFullPath());
    }

    @Test
    void testGetAlbumRating() {
        //No setters for albumRating, only from db
        assertEquals(65, fileInfoInt.getAlbumRating());
    }

    @Test
    void testGetPercentRated() {
        assertEquals(0, fileInfoInt.getPercentRated());
    }

    @Test
    void testGetLyrics() {
        assertEquals("", fileInfoInt.getLyrics());
        fileInfoInt.lyrics = "Test lyrics";
        assertEquals("Test lyrics", fileInfoInt.getLyrics());
    }

    @Test
    void testGetTitle() {
        assertEquals("", fileInfoInt.getTitle());
        fileInfoInt.title = "Test title";
        assertEquals("Test title", fileInfoInt.getTitle());
    }

    @Test
    void testGetCoverImage() {
        assertNull(fileInfoInt.getCoverImage());
    }

    @Test
    void testGetFormattedModifDate() {
        assertEquals("1970-01-01 00:00:00", fileInfoInt.getFormattedModifDate());
        fileInfoInt.modifDate = new Date(0);
        assertEquals("1970-01-01 00:00:00", fileInfoInt.getFormattedModifDate());
    }

    @Test
    void testGetYear() {
        assertEquals("", fileInfoInt.getYear());
        fileInfoInt.year = "2023";
        assertEquals("2023", fileInfoInt.getYear());
    }

    @Test
    void testGetArtist() {
        assertEquals("", fileInfoInt.getArtist());
        fileInfoInt.artist = "Test artist";
        assertEquals("Test artist", fileInfoInt.getArtist());
    }

    @Test
    void testGetAlbumArtist() {
        assertEquals("", fileInfoInt.getAlbumArtist());
        fileInfoInt.albumArtist = "Test album artist";
        assertEquals("Test album artist", fileInfoInt.getAlbumArtist());
    }

    @Test
    void testGetAlbum() {
        assertEquals("", fileInfoInt.getAlbum());
        fileInfoInt.album = "Test album";
        assertEquals("Test album", fileInfoInt.getAlbum());
    }

    @Test
    void testGetCoverHash() {
        assertEquals("", fileInfoInt.getCoverHash());
        fileInfoInt.setCoverHash("testhash");
        assertEquals("testhash", fileInfoInt.getCoverHash());
    }

    @Test
    void testSetCoverHash() {
        fileInfoInt.setCoverHash("newhash");
        assertEquals("newhash", fileInfoInt.getCoverHash());
    }

    @Test
    void testGetReplayGain() {
        GainValues gainValues = new GainValues(12.1f, 53.6f);
        assertEquals(gainValues, fileInfoInt.getReplayGain(false));
        assertEquals(gainValues, fileInfoInt.getReplayGain(true));
    }

    @Test
    void testFileInfoIntFromFileInfo() {
        FileInfo fileInfo = new FileInfo(1, 1, "test/path", 5, "2023-01-01 00:00:00", "2023-01-01 00:00:00", 10, "source", 5, 120.0f, "Test Genre", "2023-01-01 00:00:00", "2023-01-01 00:00:00", "2023-01-01 00:00:00");
        FileInfoInt fileInfoIntFromFileInfo = new FileInfoInt(fileInfo, 120.0f, "Test Genre");
        assertEquals(fileInfo.getIdFile(), fileInfoIntFromFileInfo.getIdFile());
        assertEquals(fileInfo.getIdPath(), fileInfoIntFromFileInfo.getIdPath());
        assertEquals(fileInfo.getRelativeFullPath(), fileInfoIntFromFileInfo.getRelativeFullPath());
        assertEquals(fileInfo.getRating(), fileInfoIntFromFileInfo.getRating());
        assertEquals(fileInfo.getFormattedLastPlayed(), fileInfoIntFromFileInfo.getFormattedLastPlayed());
        assertEquals(fileInfo.getFormattedAddedDate(), fileInfoIntFromFileInfo.getFormattedAddedDate());
        assertEquals(fileInfo.getPlayCounter(), fileInfoIntFromFileInfo.getPlayCounter());
        assertEquals(fileInfo.getSourceName(), fileInfoIntFromFileInfo.getSourceName());
        assertEquals(fileInfo.getPreviousPlayCounter(), fileInfoIntFromFileInfo.getPreviousPlayCounter());
        assertEquals(fileInfo.getBPM(), fileInfoIntFromFileInfo.getBPM());
        assertEquals(fileInfo.getFormattedRatingModifDate(), fileInfoIntFromFileInfo.getFormattedRatingModifDate());
        assertEquals(fileInfo.getFormattedTagsModifDate(), fileInfoIntFromFileInfo.getFormattedTagsModifDate());
        assertEquals(fileInfo.getFormattedGenreModifDate(), fileInfoIntFromFileInfo.getFormattedGenreModifDate());
    }
}