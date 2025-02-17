package jamuz;


import java.util.Date;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class FileInfoTest {

	@Test
	public void testGetIdFile() {
		FileInfo fileInfo = new FileInfo("source", "path");
		fileInfo.setIdFile(1);
		assertEquals(1, fileInfo.getIdFile());
	}

	@Test
	public void testSetIdFile() {
		FileInfo fileInfo = new FileInfo("source", "path");
		fileInfo.setIdFile(2);
		assertEquals(2, fileInfo.getIdFile());
	}

	@Test
	public void testGetLastPlayed() {
		FileInfo fileInfo = new FileInfo("source", "path");
		Date now = new Date();
		fileInfo.setLastPlayed(now);
		assertEquals(now, fileInfo.getLastPlayed());
	}

	@Test
	public void testSetLastPlayed() {
		FileInfo fileInfo = new FileInfo("source", "path");
		Date now = new Date();
		fileInfo.setLastPlayed(now);
		assertEquals(now, fileInfo.getLastPlayed());
	}

	@Test
	public void testEquals() {
		FileInfo fileInfo1 = new FileInfo("source", "path1");
		FileInfo fileInfo2 = new FileInfo("source", "path1");
		assertTrue(fileInfo1.equals(fileInfo2));
	}

	@Test
	public void testNotEquals() {
		FileInfo fileInfo1 = new FileInfo("source", "path1");
		FileInfo fileInfo2 = new FileInfo("source", "path2");
		assertFalse(fileInfo1.equals(fileInfo2));
	}

	@Test
	public void testHashCode() {
		FileInfo fileInfo1 = new FileInfo("source", "path1");
		FileInfo fileInfo2 = new FileInfo("source", "path1");
		assertEquals(fileInfo1.hashCode(), fileInfo2.hashCode());
	}

	@Test
	public void testClone() throws CloneNotSupportedException {
		FileInfo fileInfo1 = new FileInfo("source", "path1");
		FileInfo fileInfo2 = (FileInfo) fileInfo1.clone();
		assertEquals(fileInfo1, fileInfo2);
		assertNotSame(fileInfo1, fileInfo2);
	}
}
