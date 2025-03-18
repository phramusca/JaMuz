package jamuz;

import jamuz.process.check.FolderInfo;
import jamuz.process.sync.SyncStatus;
import jamuz.utils.DateTime;
import jamuz.process.check.FolderInfo.CheckedFlag;
import jamuz.process.check.ReplayGain.GainValues;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.Date;
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

    private FileInfoInt fileInfoIntFromDb;
    private FileInfoInt fileInfoIntForScan;

    @BeforeEach
    void setUp() {
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
                "2012-04-07 12:15:28", "1956-12-25 22:08:58", "2056-11-18 15:34:12", 
                "test cover hash", CheckedFlag.OK_WARNING, 
                FolderInfo.CopyRight.NO_SUPPORT, 65, 84, "test toot path", SyncStatus.NEW, "1243-05-30 19:26:42", "test path mbid", new GainValues(12.1f, 53.6f));
        
        fileInfoIntForScan = new FileInfoInt("path/to/file.mp3", "/home/user/music");
    }

    @Test
    void testGetReplayGain() {
        GainValues gainValues = new GainValues(12.1f, 53.6f);
        assertEquals(gainValues, fileInfoIntFromDb.getReplayGain(false));

        // Mocking AudioFileIO for readReplayGainFromFlac
        MockedStatic<AudioFileIO> mockedStatic = Mockito.mockStatic(AudioFileIO.class);
        mockedStatic.when(() -> AudioFileIO.read(any(File.class))).thenReturn(mock(MP3File.class));

        assertEquals(new GainValues(), fileInfoIntForScan.getReplayGain(false));
        mockedStatic.close();

        FileInfoInt fileInfoIntMock = mock(FileInfoInt.class);
        when(fileInfoIntMock.getReplayGain(true)).thenReturn(gainValues);
        assertEquals(gainValues, fileInfoIntMock.getReplayGain(true));
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
        fileInfoIntForScan.readMetadata(false);

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

    //FIXME: Test pathMbId (not used ??)

    //FIXME: Test pathModifDate (not used ??)

    @Test
    void testGetStatus() {
        assertEquals(SyncStatus.NEW, fileInfoIntFromDb.getStatus());
        assertEquals(SyncStatus.INFO, fileInfoIntForScan.getStatus());
        fileInfoIntFromDb.setStatus(SyncStatus.INFO);
        assertEquals(SyncStatus.INFO, fileInfoIntFromDb.getStatus());
    }

    @Test
    void testGetPercentRated() {
        assertEquals(84, fileInfoIntFromDb.getPercentRated());
        assertEquals(0, fileInfoIntForScan.getPercentRated());
    }

    @Test
    void testGetAlbumRating() {
        assertEquals(65, fileInfoIntFromDb.getAlbumRating());
        assertEquals(0.0, fileInfoIntForScan.getAlbumRating());
    }

    @Test
    void testGetCopyRight() {
        assertEquals(FolderInfo.CopyRight.NO_SUPPORT, fileInfoIntFromDb.getCopyRight());
        assertEquals(FolderInfo.CopyRight.UNDEFINED, fileInfoIntForScan.getCopyRight());
    }

    @Test
    void testGetCheckedFlag() {
        assertEquals(CheckedFlag.OK_WARNING, fileInfoIntFromDb.getCheckedFlag());
        assertEquals(CheckedFlag.UNCHECKED, fileInfoIntForScan.getCheckedFlag());
    }

    @Test
    void testGetCoverHash() {
        assertEquals("test cover hash", fileInfoIntFromDb.getCoverHash());
        assertEquals("", fileInfoIntForScan.getCoverHash());
        fileInfoIntFromDb.setCoverHash("testhash");
        assertEquals("testhash", fileInfoIntFromDb.getCoverHash());
    }

    @Test
    void testGetFormattedModifDate() {
        //FIXME: Test other getters and setters
        // fileInfoIntFromDb.getFormattedAddedDate();

        assertEquals("2056-11-18 15:34:12", fileInfoIntFromDb.getFormattedModifDate());
        assertEquals("1970-01-01 00:00:00", fileInfoIntForScan.getFormattedModifDate());
        fileInfoIntFromDb.modifDate = new Date(1861920000000L);
        assertEquals("2029-01-01 00:00:00", fileInfoIntFromDb.getFormattedModifDate());
    }

    @Test
    void testGetLastPlayed() {
        //FIXME: Test other getters and setters
        // fileInfoIntFromDb.getFormattedAddedDate();

        assertEquals(DateTime.parseSqlUtc("1956-12-25 22:08:58"), fileInfoIntFromDb.getLastPlayed());
        assertEquals(DateTime.parseSqlUtc("1970-01-01 00:00:00"), fileInfoIntForScan.getLastPlayed());
        fileInfoIntFromDb.lastPlayed = new Date(1861920000000L);
        assertEquals(DateTime.parseSqlUtc("2029-01-01 00:00:00"), fileInfoIntFromDb.getLastPlayed());
        fileInfoIntFromDb.setLastPlayed(new Date(1672531200000L));
        assertEquals(DateTime.parseSqlUtc("2023-01-01 00:00:00"), fileInfoIntFromDb.getLastPlayed());
    }

    @Test
    void testGetAddedDate() {
        //FIXME: Test other getters and setters
        // fileInfoIntFromDb.getFormattedAddedDate();


        assertEquals(DateTime.parseSqlUtc("2012-04-07 12:15:28"), fileInfoIntFromDb.getAddedDate());
        assertEquals(DateTime.parseSqlUtc("1970-01-01 00:00:00"), fileInfoIntForScan.getAddedDate());
        fileInfoIntFromDb.addedDate = new Date(1861920000000L);
        assertEquals(DateTime.parseSqlUtc("2029-01-01 00:00:00"), fileInfoIntFromDb.getAddedDate());
        fileInfoIntFromDb.setAddedDate(new Date(1672531200000L));
        assertEquals(DateTime.parseSqlUtc("2023-01-01 00:00:00"), fileInfoIntFromDb.getAddedDate());
    }

    @Test
    void testGetRating() {
        assertEquals(12, fileInfoIntFromDb.getRating());
        assertEquals(-1, fileInfoIntForScan.getRating());
        fileInfoIntFromDb.rating = 5;
        assertEquals(5, fileInfoIntFromDb.getRating());
    }

    @Test
    void testGetPlayCounter() {
        assertEquals(15, fileInfoIntFromDb.getPlayCounter());
        assertEquals(0, fileInfoIntForScan.getPlayCounter());
        fileInfoIntFromDb.playCounter = 5;
        assertEquals(5, fileInfoIntFromDb.getPlayCounter());
    }

    @Test
    void testGetYear() {
        assertEquals("test year", fileInfoIntFromDb.getYear());
        assertEquals("", fileInfoIntForScan.getYear());
        fileInfoIntFromDb.year = "2023";
        assertEquals("2023", fileInfoIntFromDb.getYear());
    }

    @Test
    void testGetTrackTotal() {
        assertEquals(9, fileInfoIntFromDb.getTrackTotal());
        assertEquals(-1, fileInfoIntForScan.getTrackTotal());
        fileInfoIntFromDb.trackTotal = 10;
        assertEquals(10, fileInfoIntFromDb.getTrackTotal());
    }

    @Test
    void testGetTrackNo() {
        assertEquals(4, fileInfoIntFromDb.getTrackNo());
        assertEquals(-1, fileInfoIntForScan.getTrackNo());
        fileInfoIntFromDb.trackNo = 5;
        assertEquals(5, fileInfoIntFromDb.getTrackNo());
    }

    @Test
    void testGetTitle() {
        assertEquals("test title", fileInfoIntFromDb.getTitle());
        assertEquals("", fileInfoIntForScan.getTitle());
        fileInfoIntFromDb.title = "une autre valeur de titre";
        assertEquals("une autre valeur de titre", fileInfoIntFromDb.getTitle());
    }

    @Test
    void testGetNbCovers() {
        assertEquals(84, fileInfoIntFromDb.getNbCovers());
        assertEquals(0, fileInfoIntForScan.getNbCovers());
        fileInfoIntFromDb.nbCovers = 3;
        assertEquals(3, fileInfoIntFromDb.getNbCovers());
    }

    @Test
    void testGetGenre() {
        assertEquals("test genre", fileInfoIntFromDb.getGenre());
        assertEquals("", fileInfoIntForScan.getGenre());
        fileInfoIntFromDb.genre = "n'importe quoi comme genre";
        assertEquals("n'importe quoi comme genre", fileInfoIntFromDb.getGenre());
    }

    @Test
    void testGetDiscTotal() {
        assertEquals(14, fileInfoIntFromDb.getDiscTotal());
        assertEquals(-1, fileInfoIntForScan.getDiscTotal());
        fileInfoIntFromDb.discTotal = 48;
        assertEquals(48, fileInfoIntFromDb.getDiscTotal());
    }
    
    @Test
    void testGetDiscNo() {
        assertEquals(2, fileInfoIntFromDb.getDiscNo());
        assertEquals(-1, fileInfoIntForScan.getDiscNo());
        fileInfoIntFromDb.discNo = 521;
        assertEquals(521, fileInfoIntFromDb.getDiscNo());
    }

    @Test
    void testGetComment() {
        assertEquals("test comment", fileInfoIntFromDb.getComment());
        assertEquals("", fileInfoIntForScan.getComment());
        fileInfoIntFromDb.comment = "Ceci n'est pas un commentaire";
        assertEquals("Ceci n'est pas un commentaire", fileInfoIntFromDb.getComment());
    }

    @Test
    void testGetArtist() {
        assertEquals("test artist", fileInfoIntFromDb.getArtist());
        assertEquals("", fileInfoIntForScan.getArtist());
        fileInfoIntFromDb.artist = "un peu n'importe quoi comme artiste";
        assertEquals("un peu n'importe quoi comme artiste", fileInfoIntFromDb.getArtist());
    }

    @Test
    void testGetAlbumArtist() {
        assertEquals("test album artist", fileInfoIntFromDb.getAlbumArtist());
        assertEquals("", fileInfoIntForScan.getAlbumArtist());
        fileInfoIntFromDb.albumArtist = "n'importe quoi comme artiste";
        assertEquals("n'importe quoi comme artiste", fileInfoIntFromDb.getAlbumArtist());
    }

    @Test
    void testGetAlbum() {
        assertEquals("test album", fileInfoIntFromDb.getAlbum());
        assertEquals("", fileInfoIntForScan.getAlbum());
        fileInfoIntFromDb.album = "Modi f zefzef efaefa";
        assertEquals("Modi f zefzef efaefa", fileInfoIntFromDb.getAlbum());
    }

    @Test
    void testGetBPM() {
        assertEquals(62.15f, fileInfoIntFromDb.getBPM());
        assertEquals(0.0f, fileInfoIntForScan.getBPM());
        fileInfoIntFromDb.setBPM(120.0f);
        assertEquals(120.0f, fileInfoIntFromDb.getBPM());
    }
    
    @Test
    void testGetSize() {
        assertEquals(1598798, fileInfoIntFromDb.getSize());
        assertEquals(0, fileInfoIntForScan.getSize());
        fileInfoIntFromDb.size = 123456L;
        assertEquals(123456L, fileInfoIntFromDb.getSize());
    }

    @Test
    void testGetBitRate() {
        assertEquals("84.544", fileInfoIntFromDb.getBitRate());
        assertEquals("", fileInfoIntForScan.getBitRate());
    }

    @Test
    void testGetFormat() {
        assertEquals("format inconnu", fileInfoIntFromDb.getFormat());
        assertEquals("", fileInfoIntForScan.getFormat());
    }
    
    @Test
    void testGetLength() {
        assertEquals(5154, fileInfoIntFromDb.getLength());
        assertEquals(0, fileInfoIntForScan.getLength());
        fileInfoIntFromDb.length = 300;
        assertEquals(300, fileInfoIntFromDb.getLength());
    }

    //FIXME Test relativePath
    //FIXME Test filename
    //FIXME Test rootPath

    @Test
    void testIdPath() {
        assertEquals(24, fileInfoIntFromDb.getIdPath());
        assertEquals(-1, fileInfoIntForScan.getIdPath());
        fileInfoIntFromDb.setIdPath(217);
        assertEquals(217, fileInfoIntFromDb.getIdPath());
    }

    @Test
    void testIdFile() {
        assertEquals(512, fileInfoIntFromDb.getIdFile());
        assertEquals(-1, fileInfoIntForScan.getIdFile());
        fileInfoIntFromDb.setIdFile(665);
        assertEquals(665, fileInfoIntFromDb.getIdFile());
    }

    //FIXME: Review above tests and check if all setters are tested

    //FIXME: Review the following tests, and make sure none are missing

    

  
    @Test
    void testGetFullPath() {
        File expectedFile = new File("root/path/test/path");
        assertEquals(expectedFile, fileInfoIntFromDb.getFullPath());
        assertEquals(new File("/home/user/music/path/to/file.flac"), fileInfoIntForScan.getFullPath());
    }

    @Test
    void testGetLyrics() {
        assertEquals("", fileInfoIntFromDb.getLyrics());
        assertEquals("", fileInfoIntForScan.getLyrics());
        fileInfoIntFromDb.lyrics = "Test lyrics";
        assertEquals("Test lyrics", fileInfoIntFromDb.getLyrics());
    }

    @Test
    void testGetCoverImage() {
        assertNull(fileInfoIntFromDb.getCoverImage());
        assertNull(fileInfoIntForScan.getCoverImage());
    }

    @Test
    void testSetCoverHash() {
        fileInfoIntFromDb.setCoverHash("newhash");
        assertEquals("newhash", fileInfoIntFromDb.getCoverHash());
        fileInfoIntForScan.setCoverHash("newhash");
        assertEquals("newhash", fileInfoIntForScan.getCoverHash());
    }

    @Test
    void testFileInfoIntFromFileInfo() {

        //FIXME: Check if all fields are tested

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