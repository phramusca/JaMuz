package jamuz.soulseek;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SlskdSearchFileTest {

    private SlskdSearchFile searchFile;

    @Before
    public void setUp() {
        searchFile = new SlskdSearchFile();
    }

    @Test
    public void testGetDateWithEndedAt() {
        searchFile.endedAt = "2023-10-10T10:00:00.000Z";
        assertEquals("10/10/2023 12:00:00", searchFile.getDate());
    }

    @Test
    public void testGetDateWithStartedAt() {
        searchFile.startedAt = "2023-10-10T09:00:00.000Z";
        assertEquals("10/10/2023 11:00:00", searchFile.getDate());
    }

    @Test
    public void testGetDateWithEnqueuedAt() {
        searchFile.enqueuedAt = "2023-10-10T08:00:00.000Z";
        assertEquals("10/10/2023 10:00:00", searchFile.getDate());
    }

    @Test
    public void testGetDateWithRequestedAt() {
        searchFile.requestedAt = "2023-10-10T07:00:00.000Z";
        assertEquals("10/10/2023 09:00:00", searchFile.getDate());
    }

    @Test
    public void testGetDateWithSearchedAt() {
        searchFile.searchedAt = "NoMatterWhat";
        assertEquals("NoMatterWhat", searchFile.getDate());
    }

    @Test
    public void testGetDateWithNoDates() {
        assertEquals("null", searchFile.getDate());
    }
}