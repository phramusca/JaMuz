package jamuz.utils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

class XMLTest {

    @Test
    void shouldOpenAndReadXmlElements() throws Exception {
        Path file = Files.createTempFile("xml", ".xml");
        Files.writeString(file, "<root><item id=\"1\">v</item><item id=\"2\">w</item></root>", StandardCharsets.UTF_8);

        Document doc = XML.open(file.toString());
        assertNotNull(doc);
        assertEquals("v", XML.getNodeValue(doc, "root", "item"));
        assertEquals(2, XML.getElements(doc, "item").size());

        Element item = XML.getElement(doc.getDocumentElement(), "item");
        assertEquals("v", XML.getElementValue(item));
        assertEquals("1", XML.getAttribute(item, "id"));
    }
}
