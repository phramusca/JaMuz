package jamuz.process.book;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void shouldExposeBasicMetadataAndSortByTitleSort() {
        Book a = new Book("A", "A", "2020", "x", "uuid", "p", "c", "5", "en", "auth", "f", "EPUB/PDF", "tag1/tag2");
        Book b = new Book("B", "B", "2021", "y", "uuid2", "p2", "c2", "4", "en", "auth2", "f2", "PDF", "tag2");

        assertTrue(a.getFormat().contains("epub"));
        assertEquals("A", a.getTitle());
        assertFalse(a.isLocal());
        a.setLength(10);
        assertTrue(a.isLocal());
        assertFalse(a.getTags().isEmpty());
        assertTrue(a.compareTo(b) < 0);
        assertNotNull(a.toString());
    }
}
