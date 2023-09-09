package vip.sujianfeng.utils.comm;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.URL;

/**
 * author SuJianFeng
 * createTime  2019/9/11 7:50
 **/
public class XmlUtils {

    public static <T> T parseResourceFile(Class<T> clazz, String resourceFile) throws Exception {
        //InputStream is = this.getClass().getResourceAsStream(jarFileName);
        URL url = XmlUtils.class.getResource(resourceFile);
        if (url == null){
            throw new Exception("not found: " + resourceFile);
        }
        return parseURL(clazz, url);
    }

    public static <T> T parseURL(Class<T> clazz, URL url) throws Exception {
        InputStream is = new FileInputStream(url.getFile());
        return parseInputStream(clazz, is);
    }

    @SuppressWarnings("unchecked")
    public static <T> T parseInputStream(Class<T> clazz, InputStream dataStream) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(clazz);
        Unmarshaller u = jc.createUnmarshaller();
        return (T) u.unmarshal(dataStream);
    }

    @SuppressWarnings("unchecked")
    public static <T> T parseInputString(Class<T> clazz, String dataStr) throws JAXBException, UnsupportedEncodingException {
        JAXBContext jc = JAXBContext.newInstance(clazz);
        Unmarshaller u = jc.createUnmarshaller();
        return (T) u.unmarshal(new ByteArrayInputStream(dataStr.getBytes("UTF-8")));
    }

    @SuppressWarnings("unchecked")
    public static <T> T parseFile(Class<T> clazz, String fileName) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(clazz);
        Unmarshaller u = jc.createUnmarshaller();
        return (T) u.unmarshal(new File(fileName));
    }

    public static String convertToXmlStr(Object obj) throws Exception {
        //Create output stream
        StringWriter sw = new StringWriter();
        sw.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n");
        JAXBContext context = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        marshaller.marshal(obj, sw);
        return sw.toString();
    }

    public static String convertToXmlStrWithXsi(Object obj, String schemaLocation) throws Exception {
        StringWriter sw = new StringWriter();
        sw.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n");
        JAXBContext context = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, schemaLocation);
        marshaller.marshal(obj, sw);
        return sw.toString();
    }
}
