package jamuz.soulseek;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SlskdSearchResultTest {

    @Test
    void record_accessorsReturnConstructorValues() {
        SlskdSearchResult searchResult = new SlskdSearchResult(
            10,
            "12345",
            true,
            2,
            5,
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
        assertEquals("testSearch", searchResult.searchText());
        assertEquals("2023-10-10T10:00:00Z", searchResult.startedAt());
        assertEquals("completed", searchResult.state());
        assertEquals(123, searchResult.token());
    }
}
