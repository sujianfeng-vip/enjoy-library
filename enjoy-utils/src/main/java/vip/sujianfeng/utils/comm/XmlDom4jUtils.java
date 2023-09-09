package vip.sujianfeng.utils.comm;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.SAXException;

import java.io.*;
import java.net.URL;
import java.util.List;

public class XmlDom4jUtils {

	private static SAXReader getNewSAXReader() throws SAXException {
		SAXReader reader = new SAXReader();
		// Do not load DTD to prevent errors from attempting to find DTD on the network during network outages
		reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		return reader;
	}

	public static Document read(String fileName) throws Exception {
		SAXReader reader = getNewSAXReader();
		Document document = reader.read(new File(fileName));
		return document;
	}

	public static Document parseResFile(String resFile) throws Exception {
		return readFromJar(resFile);
	}
	public static Document readFromJar(String jarFileName) throws Exception {

		//This sentence will have a cache
		//InputStream is = this.getClass().getResourceAsStream(jarFileName);
		//The following sentence does not have a cache
		URL url = XmlDom4jUtils.class.getResource(jarFileName);
		if (url == null){
			throw new Exception("File not found:" + jarFileName);
		}
		return parseURL(url);
	}

	public static Document parseURL(URL url) throws Exception {
		InputStream is = new FileInputStream(url.getFile());
		return parseInputStream(is);
	}

	public static Document parseInputStream(InputStream is ) throws Exception {
		SAXReader reader = getNewSAXReader();
		Document document = reader.read(is);
		return document;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Element> getElementsByName(Document doc, String name) {
		if(StringUtilsEx.isEmpty(name))
			return null;
		String xpathExp = "//*[local-name()='"+name+"']";
		if(name.indexOf(':')>-1) {
			String nsPre = name.substring(0, name.indexOf(':'));
			String localName = name.substring(name.indexOf(':')+1);
			xpathExp = "//*[name()='"+nsPre+":"+localName+"']";
		}
		return doc.selectNodes(xpathExp);
	}

	public static void save(Document doc, String fileName) throws IOException{
		FileWriter fileWriter = new FileWriter(fileName);
		//document.write(fileWriter);	
		//FileWorker.saveTxtFile(fileName, document.asXML());
		OutputFormat format;
		// 美化格式
		 format = OutputFormat.createPrettyPrint();
		// 缩减格式
		//format = OutputFormat.createCompactFormat();
		XMLWriter xmlWriter = new XMLWriter(fileWriter, format);
		xmlWriter.write(doc);
		xmlWriter.close();		
	}
	
	public static String getAttribute(Element element, String name){
		for (Object o : element.attributes()) {
			Attribute attribute = (Attribute) o;
			if (StringUtilsEx.sameText(name, attribute.getName()) ||
					StringUtilsEx.sameText(name, attribute.getQualifiedName())){
				return attribute.getValue();
			}
		}
		if (element.element(name) != null)
			return element.elementText(name);
		return "";
	}	

	public static String formatXML(Element element) throws IOException{
		OutputFormat format = OutputFormat.createPrettyPrint();
		StringWriter out = new StringWriter();
		XMLWriter writer = new XMLWriter(out, format);
		writer.write(element);
		return out.toString();
	}

	public static void main(String[] args) {

	}

}
