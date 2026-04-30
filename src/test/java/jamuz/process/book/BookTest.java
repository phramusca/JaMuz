package jamuz.process.book;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    private static Book book(String title, String titleSort) {
        return new Book(title, titleSort, "2020", "sort-author", "uuid-1",
                "/path/", "cover.jpg", "5", "en", "Author Name", "file.epub",
                "EPUB/PDF", "fiction/fantasy");
    }

    @Test
    void constructor_exposesTitle() {
        assertEquals("Dune", book("Dune", "Dune").getTitle());
    }

    @Test
    void getFormat_isLowerCase() {
        String fmt = book("X", "X").getFormat();
        assertTrue(fmt.contains("epub") || fmt.contains("pdf"),
                "Format should contain epub or pdf");
    }

    @Test
    void isLocal_falseByDefault_trueAfterSetLength() {
        Book b = book("A", "A");
        assertFalse(b.isLocal());
        b.setLength(100L);
        assertTrue(b.isLocal());
    }

    @Test
    void getTags_isNotEmpty() {
        assertFalse(book("A", "A").getTags().isEmpty());
    }

    @Test
    void isSelected_falseByDefault() {
        assertFalse(book("A", "A").isSelected());
    }

    @Test
    void setSelected_updatesState() {
        Book b = book("A", "A");
        b.setSelected(true);
        assertTrue(b.isSelected());
    }

    @Test
    void compareTo_sortsByTitleSort() {
        Book a = book("Alpha Book", "alpha");
        Book b = book("Beta Book", "beta");
        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
    }

    @Test
    void toString_isNotBlank() {
        assertFalse(book("A", "A").toString().isBlank());
    }
}
