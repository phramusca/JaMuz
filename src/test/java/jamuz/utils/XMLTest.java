package jamuz.utils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

class XMLTest {

    private static final String XML_CONTENT =
            "<root><item id=\"1\">v</item><item id=\"2\">w</item></root>";

    private Document tempXml() throws Exception {
        Path file = Files.createTempFile("xml", ".xml");
        Files.writeString(file, XML_CONTENT, StandardCharsets.UTF_8);
        return XML.open(file.toString());
    }

    @Test
    void open_withValidFile_returnsDocument() throws Exception {
        assertNotNull(tempXml());
    }

    @Test
    void getNodeValue_returnsFirstMatch() throws Exception {
        assertEquals("v", XML.getNodeValue(tempXml(), "root", "item"));
    }

    @Test
    void getElements_returnsAllMatchingElements() throws Exception {
        List<Element> elements = XML.getElements(tempXml(), "item");
        assertEquals(2, elements.size());
    }

    @Test
    void getElements_withUnknownTag_returnsEmptyList() throws Exception {
        List<Element> elements = XML.getElements(tempXml(), "unknown");
        assertTrue(elements.isEmpty());
    }

    @Test
    void getElement_returnsFirstChild() throws Exception {
        Document doc = tempXml();
        Element item = XML.getElement(doc.getDocumentElement(), "item");
        assertNotNull(item);
        assertEquals("v", XML.getElementValue(item));
    }

    @Test
    void getElement_withUnknownTag_returnsNull() throws Exception {
        Document doc = tempXml();
        assertNull(XML.getElement(doc.getDocumentElement(), "nope"));
    }

    @Test
    void getAttribute_withKnownAttr_returnsValue() throws Exception {
        Document doc = tempXml();
        Element item = XML.getElement(doc.getDocumentElement(), "item");
        assertEquals("1", XML.getAttribute(item, "id"));
    }

    @Test
    void getAttribute_withUnknownAttr_returnsEmptyString() throws Exception {
        Document doc = tempXml();
        Element item = XML.getElement(doc.getDocumentElement(), "item");
        assertEquals("", XML.getAttribute(item, "noSuchAttr"));
    }
}
