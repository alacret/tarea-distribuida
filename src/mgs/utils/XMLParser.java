package mgs.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

final public class XMLParser {
	private XMLParser() {
		throw new UnsupportedOperationException(
				"This class is not instantiable");
	}

	final public static Map<String, String> parse(String readLine, Toilet toilet)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document parse = db
				.parse(new ByteArrayInputStream(readLine.getBytes()));
		NodeList childNodes = parse.getChildNodes();

		Node head = childNodes.item(0);

		if (head == null) {
			toilet.flush("Wrong message format");
			return null;
		}

		childNodes = head.getChildNodes();
		Map<String, String> params = new HashMap<String, String>();

		for (int i = 0, j = childNodes.getLength(); i < j; i++) {
			Node item = childNodes.item(i);
			params.put(item.getNodeName(), item.getTextContent());
		}
		return params;
	}
}
