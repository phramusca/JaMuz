package jamuz.soulseek;


import org.junit.Test;
import static org.junit.Assert.*;

public class SlskdSearchResultTest {

	@Test
	public void testRecord() {
		SlskdSearchResult searchResult = new SlskdSearchResult(
			10,
			"12345",
			true,
			2,
			5,
			// responses, //TODO
			"testSearch",
			"2023-10-10T10:00:00Z",
			"completed",
			123
		);

		assertEquals(10, searchResult.fileCount());
		assertEquals("12345", searchResult.id());
		assertTrue(searchResult.isComplete());
		assertEquals(2, searchResult.lockedFileCount());
		assertEquals(5, searchResult.responseCount());
		// assertEquals(responses, searchResult.responses()); //TODO
		assertEquals("testSearch", searchResult.searchText());
		assertEquals("2023-10-10T10:00:00Z", searchResult.startedAt());
		assertEquals("completed", searchResult.state());
		assertEquals(123, searchResult.token());
	}
}
