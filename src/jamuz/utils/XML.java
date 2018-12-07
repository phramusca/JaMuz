/*
 * Copyright (C) 2017 phramusca ( https://github.com/phramusca/JaMuz/ )
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package jamuz.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class XML {
	public static Document open(String filename) {
		try {
			File file = new File(filename);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
			org.w3c.dom.Document doc = documentBuilder.parse(file);
			doc.getDocumentElement().normalize();
			return doc;
		} catch (ParserConfigurationException | SAXException | IOException ex) {
			//Proper error handling. filename is displayed in ex, so no need to add it again
			Popup.error(ex); 
			return null;
        }
	}

	public static String getNodeValue(Document doc, String TagNameLev1, String TagNameLev2) {
		NodeList nodeLst = doc.getElementsByTagName(TagNameLev1);
		Node fstNode = nodeLst.item(0);

		Element myElement = (Element) fstNode;
		NodeList myElementList = myElement.getElementsByTagName(TagNameLev2);
		Element mySubElement = (Element) myElementList.item(0);
		NodeList mySubElementList = mySubElement.getChildNodes();
		return ((Node) mySubElementList.item(0)).getNodeValue();
	}
	
	public static ArrayList<Element> getElements(Document doc, String tagName) {
		ArrayList<Element> elements=new ArrayList<>();
		NodeList nodeList = doc.getElementsByTagName(tagName);
		System.out.println(nodeList.getLength());
		
		for(int i=0; i<nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			elements.add((Element) node);
		}
		return elements;
	}
	
	public static Element getElement(Element element, String tagName) {
		NodeList nodeList = element.getElementsByTagName(tagName);
		Node node = nodeList.item(0);
		return (Element) node;
	}
	
	public static String getElementValue(Element element) {
		NodeList mySubElementList = element.getChildNodes();
		return ((Node) mySubElementList.item(0)).getNodeValue();
	}
	
	public static String getAttribute(Element element, String attribute) {
		return element.getAttribute(attribute);
	}
			
	
}
