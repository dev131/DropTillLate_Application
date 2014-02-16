package ch.droptilllate.application.com;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlConnection implements IXmlConnection {

	private String path;
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	private Document document;

	public XmlConnection(String path, String rootElement) {
		this.path = path;
		File f = new File(path);
		if (!f.exists())
			createFile(path, rootElement);
	}

	/**
	 * Create File
	 * 
	 * @param path
	 * @param rootElement
	 */
	private void createFile(String path, String rootElement) {
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentFactory
					.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
			Element root = document.createElement(rootElement);
			document.appendChild(root);
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(new File(path));
			transformer.transform(domSource, streamResult);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Document getXML() {
		factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		builder = null;
		document = null;
		try {
			builder = factory.newDocumentBuilder();
			document = builder.parse(path);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return document;

	}

	@Override
	public void writeToXML() {
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		try {
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result1 = new StreamResult(new File(path));
			transformer.transform(source, result1);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	

	}

	@Override
	public NodeList executeQuery(String query) {
		NodeList nodes = null;
		XPathExpression expr = null;
		// create an XPathFactory
		XPathFactory xFactory = XPathFactory.newInstance();
		// create an XPath object
		XPath xpath = xFactory.newXPath();
		// compile the XPath expression
		// run the query and get a nodeset
		Object result = null;
		try {
			expr = xpath.compile(query);

			result = expr.evaluate(document.getDocumentElement(),
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		// cast the result to a DOM NodeList
		nodes = (NodeList) result;
		return nodes;

	}
}
