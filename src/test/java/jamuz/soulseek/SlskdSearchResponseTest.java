package jamuz.soulseek;

import jamuz.utils.DateTime;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class SlskdSearchResponseTest {

	private SlskdSearchResponse searchResponse;

	@Before
	public void setUp() {
		searchResponse = new SlskdSearchResponse();
	}

	@Test
	public void testClass() {
		List<SlskdSearchFile> files = new ArrayList<>();
		SlskdSearchFile file1 = new SlskdSearchFile("path1/file1.mp3", 320, 180, 5000000);
		SlskdSearchFile file2 = new SlskdSearchFile("path2/file2.mp3", 128, 200, 3000000);

		files.add(file1);
		files.add(file2);

		SlskdSearchResponse searchResponse = new SlskdSearchResponse();
		searchResponse.setFileCount(2);
		searchResponse.setFiles(files);
		searchResponse.setHasFreeUploadSlot(true);
		searchResponse.setLockedFileCount(1);
		searchResponse.setLockedFiles(new ArrayList<>());
		searchResponse.setQueueLength(5);
		searchResponse.setToken(123);
		searchResponse.setUploadSpeed(1.5);
		searchResponse.setUsername("testUser");
		searchResponse.setDate(DateTime.getCurrentLocal(DateTime.DateTimeFormat.HUMAN));
		searchResponse.setCompleted();
		searchResponse.setSearchText("testSearch");
		searchResponse.setQueued();
		searchResponse.setTableModelDownload(null);

		assertEquals(2, searchResponse.getFileCount());
		assertEquals(files, searchResponse.getFiles());
		assertTrue(searchResponse.hasFreeUploadSlot());
		assertEquals(1, searchResponse.getLockedFileCount());
		assertEquals(5, searchResponse.getQueueLength());
		assertEquals(123, searchResponse.getToken());
		assertEquals(1.5, searchResponse.getUploadSpeed(), 0.0);
		assertEquals("testUser", searchResponse.getUsername());
		assertTrue(searchResponse.isCompleted());
		assertEquals("testSearch", searchResponse.getSearchText());
		assertTrue(searchResponse.isQueued());
		assertNotNull(searchResponse.getProgressBar());
		assertNull(searchResponse.getTableModelDownload());
	}

	@Test
	public void testFilterAndSortFiles() {
		List<SlskdSearchFile> files = new ArrayList<>();
		SlskdSearchFile file1 = new SlskdSearchFile("path2/file2.mp3", 128, 200, 3000000);
		SlskdSearchFile file2 = new SlskdSearchFile("path1/file1.mp3", 320, 180, 5000000);
        SlskdSearchFile file3 = new SlskdSearchFile("path3/file3.flac", 320, 180, 5000000);

		files.add(file1);
		files.add(file2);
		searchResponse.setFiles(files);

		List<String> allowedExtensions = List.of("mp3");
		searchResponse.filterAndSortFiles(allowedExtensions);

		List<SlskdSearchFile> sortedFiles = searchResponse.getFiles();
		assertEquals("path1/file1.mp3", sortedFiles.get(0).getFilename());
		assertEquals("path2/file2.mp3", sortedFiles.get(1).getFilename());
	}

	@Test
	public void testCloneWithoutFiles() {
		SlskdSearchResponse clone = searchResponse.cloneWithoutFiles();
		assertEquals(0, clone.getFileCount());
		assertTrue(clone.getFiles().isEmpty());
		assertEquals(searchResponse.hasFreeUploadSlot(), clone.hasFreeUploadSlot());
		assertEquals(searchResponse.getQueueLength(), clone.getQueueLength());
		assertEquals(searchResponse.getToken(), clone.getToken());
		assertEquals(searchResponse.getUploadSpeed(), clone.getUploadSpeed(), 0.0);
		assertEquals(searchResponse.getUsername(), clone.getUsername());
		assertEquals(searchResponse.getDate(), clone.getDate());
	}

	@Test
	public void testGetBitrate() {
		List<SlskdSearchFile> files = new ArrayList<>();
		SlskdSearchFile file1 = new SlskdSearchFile("path1/file1.mp3", 320, 180, 5000000);
		SlskdSearchFile file2 = new SlskdSearchFile("path2/file2.mp3", 128, 200, 3000000);

		files.add(file1);
		files.add(file2);
		searchResponse.setFiles(files);

		assertEquals(224, searchResponse.getBitrate(), 0.0);
	}

	@Test
	public void testGetSize() {
		List<SlskdSearchFile> files = new ArrayList<>();
		SlskdSearchFile file1 = new SlskdSearchFile("path1/file1.mp3", 320, 180, 5000000);
		SlskdSearchFile file2 = new SlskdSearchFile("path2/file2.mp3", 128, 200, 3000000);

		files.add(file1);
		files.add(file2);
		searchResponse.setFiles(files);

		assertEquals(8000000, searchResponse.getSize(), 0.0);
	}

	@Test
	public void testGetPath() {
		List<SlskdSearchFile> files = new ArrayList<>();
		SlskdSearchFile file1 = new SlskdSearchFile("path1/file1.mp3", 320, 180, 5000000);

		files.add(file1);
		searchResponse.setFiles(files);

		assertEquals("path1", searchResponse.getPath());
	}

	@Test
	public void testGetTableModel() {
		List<SlskdSearchFile> files = new ArrayList<>();
		files.add(new SlskdSearchFile("path1/file1.mp3", 320, 180, 5000000));

		searchResponse.setFiles(files);

		TableModelSlskdDownload tableModel = searchResponse.getTableModel();
		assertNotNull(tableModel);
		assertEquals(1, tableModel.getRowCount());
		assertEquals("file1.mp3", tableModel.getValueAt(0, 4));
	}

	@Test
	public void testUpdate() {
		searchResponse.update("test", 1);
		assertEquals(1, searchResponse.getProgressBar().getValue());
	}
}
