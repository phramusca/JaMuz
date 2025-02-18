package jamuz;

import jamuz.process.check.FolderInfo;
import jamuz.process.sync.SyncStatus;
import jamuz.process.check.FolderInfo.CheckedFlag;
import jamuz.process.check.ReplayGain.GainValues;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.io.File;
import java.util.Date;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import java.util.Collections;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockedStatic;

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
    void testReadMetadata() throws Exception {
        // Mocking MP3File and AudioHeader
        MP3File mp3FileMock = mock(MP3File.class);
        AudioHeader audioHeaderMock = mock(AudioHeader.class);
        Tag tagMock = mock(Tag.class);
        AbstractID3v2Tag v2tagMock = mock(AbstractID3v2Tag.class);

        when(audioHeaderMock.getBitRate()).thenReturn("320kbps");
        when(audioHeaderMock.getFormat()).thenReturn("mp3");
        when(audioHeaderMock.getTrackLength()).thenReturn(300);

        when(v2tagMock.getFirst(FieldKey.ALBUM)).thenReturn("Test Album");
        when(v2tagMock.getFirst(FieldKey.ARTIST)).thenReturn("Test Artist");
        when(v2tagMock.getFirst(FieldKey.ALBUM_ARTIST)).thenReturn("Test Album Artist");
        when(v2tagMock.getFirst(FieldKey.TITLE)).thenReturn("Test Title");
        when(v2tagMock.getFirst(FieldKey.TRACK)).thenReturn("5");
        when(v2tagMock.getFirst(FieldKey.TRACK_TOTAL)).thenReturn("10");
        when(v2tagMock.getFirst(FieldKey.DISC_NO)).thenReturn("1");
        when(v2tagMock.getFirst(FieldKey.DISC_TOTAL)).thenReturn("2");
        when(v2tagMock.getFirst(FieldKey.YEAR)).thenReturn("2023");
        when(v2tagMock.getFirst(FieldKey.COMMENT)).thenReturn("Test Comment");
        when(v2tagMock.getFirst(FieldKey.GENRE)).thenReturn("Test Genre");
        when(v2tagMock.getFirst(FieldKey.BPM)).thenReturn("120");
        when(v2tagMock.getArtworkList()).thenReturn(Collections.emptyList());

        when(mp3FileMock.getAudioHeader()).thenReturn(audioHeaderMock);
        when(mp3FileMock.getTag()).thenReturn(tagMock);
        when(mp3FileMock.hasID3v2Tag()).thenReturn(true);
        when(mp3FileMock.getID3v2Tag()).thenReturn(v2tagMock);

        MockedStatic<AudioFileIO> mockedStatic = Mockito.mockStatic(AudioFileIO.class);
        mockedStatic.when(() -> AudioFileIO.read(any(File.class))).thenReturn(mp3FileMock);

        // Call the method to read metadata
        fileInfoIntFromDb.readMetadata(false);

        // Verify the values set by readHeader
        assertEquals("320kbps", fileInfoIntFromDb.getBitRate());
        assertEquals("mp3", fileInfoIntFromDb.getFormat());
        assertEquals(300, fileInfoIntFromDb.getLength());

        // Verify the values set by readTags
        assertEquals("Test Album", fileInfoIntFromDb.getAlbum());
        assertEquals("Test Artist", fileInfoIntFromDb.getArtist());
        assertEquals("Test Album Artist", fileInfoIntFromDb.getAlbumArtist());
        assertEquals("Test Title", fileInfoIntFromDb.getTitle());
        assertEquals(5, fileInfoIntFromDb.getTrackNo());
        assertEquals(10, fileInfoIntFromDb.getTrackTotal());
        assertEquals(1, fileInfoIntFromDb.getDiscNo());
        assertEquals(2, fileInfoIntFromDb.getDiscTotal());
        assertEquals("2023", fileInfoIntFromDb.getYear());
        assertEquals("Test Comment", fileInfoIntFromDb.getComment());
        assertEquals("Test Genre", fileInfoIntFromDb.getGenre());
        assertEquals(120.0f, fileInfoIntFromDb.getBPM());
    }
    
    @Test
    void testIdFile() {
        assertEquals(512, fileInfoIntFromDb.getIdFile());
        fileInfoIntFromDb.setIdFile(665);
        assertEquals(665, fileInfoIntFromDb.getIdFile());
    }
    
    @Test
    void testIdPath() {
        assertEquals(24, fileInfoIntFromDb.getIdPath());
        fileInfoIntFromDb.setIdPath(217);
        assertEquals(217, fileInfoIntFromDb.getIdPath());
    }

    @Test
    void testGetTrackNo() {
        assertEquals(-1, fileInfoIntFromDb.getTrackNo());
        fileInfoIntFromDb.trackNo = 5;
        assertEquals(5, fileInfoIntFromDb.getTrackNo());
    }

    @Test
    void testGetTrackTotal() {
        assertEquals(-1, fileInfoIntFromDb.getTrackTotal());
        fileInfoIntFromDb.trackTotal = 10;
        assertEquals(10, fileInfoIntFromDb.getTrackTotal());
    }

    @Test
    void testGetDiscNo() {
        assertEquals(-1, fileInfoIntFromDb.getDiscNo());
        fileInfoIntFromDb.discNo = 1;
        assertEquals(1, fileInfoIntFromDb.getDiscNo());
    }

    @Test
    void testGetDiscTotal() {
        assertEquals(-1, fileInfoIntFromDb.getDiscTotal());
        fileInfoIntFromDb.discTotal = 2;
        assertEquals(2, fileInfoIntFromDb.getDiscTotal());
    }

    @Test
    void testGetComment() {
        assertEquals("", fileInfoIntFromDb.getComment());
        fileInfoIntFromDb.comment = "Test comment";
        assertEquals("Test comment", fileInfoIntFromDb.getComment());
    }

    @Test
    void testGetNbCovers() {
        assertEquals(0, fileInfoIntFromDb.getNbCovers());
        fileInfoIntFromDb.nbCovers = 3;
        assertEquals(3, fileInfoIntFromDb.getNbCovers());
    }

    @Test
    void testGetCheckedFlag() {
        assertEquals(CheckedFlag.UNCHECKED, fileInfoIntFromDb.getCheckedFlag());
        assertEquals(CheckedFlag.OK, fileInfoIntFromDb.getCheckedFlag());
    }

    @Test
    void testGetBitRate() {
        assertEquals("", fileInfoIntFromDb.getBitRate());
        assertEquals("320kbps", fileInfoIntFromDb.getBitRate());
    }

    @Test
    void testGetFormat() {
        assertEquals("", fileInfoIntFromDb.getFormat());
        assertEquals("mp3", fileInfoIntFromDb.getFormat());
    }

    @Test
    void testGetLength() {
        assertEquals(0, fileInfoIntFromDb.getLength());
        fileInfoIntFromDb.length = 300;
        assertEquals(300, fileInfoIntFromDb.getLength());
    }

    @Test
    void testGetSize() {
        assertEquals(0, fileInfoIntFromDb.getSize());
        fileInfoIntFromDb.size = 123456L;
        assertEquals(123456L, fileInfoIntFromDb.getSize());
    }

    @Test
    void testGetStatus() {
        assertEquals(SyncStatus.NEW, fileInfoIntFromDb.getStatus());
        fileInfoIntFromDb.setStatus(SyncStatus.INFO);
        assertEquals(SyncStatus.INFO, fileInfoIntFromDb.getStatus());
    }

    @Test
    void testSetStatus() {
        fileInfoIntFromDb.setStatus(SyncStatus.NEW);
        assertEquals(SyncStatus.NEW, fileInfoIntFromDb.getStatus());
    }

    @Test
    void testGetFullPath() {
        File expectedFile = new File("root/path/test/path");
        assertEquals(expectedFile, fileInfoIntFromDb.getFullPath());
    }

    @Test
    void testGetAlbumRating() {
        //No setters for albumRating, only from db
        assertEquals(65, fileInfoIntFromDb.getAlbumRating());
    }

    @Test
    void testGetPercentRated() {
        assertEquals(0, fileInfoIntFromDb.getPercentRated());
    }

    @Test
    void testGetLyrics() {
        assertEquals("", fileInfoIntFromDb.getLyrics());
        fileInfoIntFromDb.lyrics = "Test lyrics";
        assertEquals("Test lyrics", fileInfoIntFromDb.getLyrics());
    }

    @Test
    void testGetTitle() {
        assertEquals("", fileInfoIntFromDb.getTitle());
        fileInfoIntFromDb.title = "Test title";
        assertEquals("Test title", fileInfoIntFromDb.getTitle());
    }

    @Test
    void testGetCoverImage() {
        assertNull(fileInfoIntFromDb.getCoverImage());
    }

    @Test
    void testGetFormattedModifDate() {
        assertEquals("1970-01-01 00:00:00", fileInfoIntFromDb.getFormattedModifDate());
        fileInfoIntFromDb.modifDate = new Date(0);
        assertEquals("1970-01-01 00:00:00", fileInfoIntFromDb.getFormattedModifDate());
    }

    @Test
    void testGetYear() {
        assertEquals("", fileInfoIntFromDb.getYear());
        fileInfoIntFromDb.year = "2023";
        assertEquals("2023", fileInfoIntFromDb.getYear());
    }

    @Test
    void testGetArtist() {
        assertEquals("", fileInfoIntFromDb.getArtist());
        fileInfoIntFromDb.artist = "Test artist";
        assertEquals("Test artist", fileInfoIntFromDb.getArtist());
    }

    @Test
    void testGetAlbumArtist() {
        assertEquals("", fileInfoIntFromDb.getAlbumArtist());
        fileInfoIntFromDb.albumArtist = "Test album artist";
        assertEquals("Test album artist", fileInfoIntFromDb.getAlbumArtist());
    }

    @Test
    void testGetAlbum() {
        assertEquals("", fileInfoIntFromDb.getAlbum());
        fileInfoIntFromDb.album = "Test album";
        assertEquals("Test album", fileInfoIntFromDb.getAlbum());
    }

    @Test
    void testGetCoverHash() {
        assertEquals("", fileInfoIntFromDb.getCoverHash());
        fileInfoIntFromDb.setCoverHash("testhash");
        assertEquals("testhash", fileInfoIntFromDb.getCoverHash());
    }

    @Test
    void testSetCoverHash() {
        fileInfoIntFromDb.setCoverHash("newhash");
        assertEquals("newhash", fileInfoIntFromDb.getCoverHash());
    }

    @Test
    void testGetReplayGain() {
        GainValues gainValues = new GainValues(12.1f, 53.6f);
        assertEquals(gainValues, fileInfoIntFromDb.getReplayGain(false));
        assertEquals(gainValues, fileInfoIntFromDb.getReplayGain(true));
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