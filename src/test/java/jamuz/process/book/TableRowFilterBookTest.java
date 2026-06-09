package jamuz.process.book;

import javax.swing.RowFilter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TableRowFilterBookTest {

    private static final class BookEntry extends RowFilter.Entry<Object, Object> {
        private final Book book;
        BookEntry(Book book) { this.book = book; }
        @Override public Object getModel() { return null; }
        @Override public int getValueCount() { return 3; }
        @Override public Object getValue(int index) { return index == 2 ? book : null; }
        @Override public String getStringValue(int index) { return String.valueOf(getValue(index)); }
        @Override public Object getIdentifier() { return 1; }
    }

    @Test
    void shouldIncludeBookByDefault() {
        Book book = new Book("A", "A", "2020", "x", "uuid", "p", "c", "5", "en", "auth", "f", "EPUB", "tag1");
        TableRowFilterBook filter = new TableRowFilterBook();

        assertTrue(filter.include(new BookEntry(book)));
    }
}
