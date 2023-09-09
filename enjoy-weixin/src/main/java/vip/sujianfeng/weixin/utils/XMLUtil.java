package vip.sujianfeng.weixin.utils;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by sujianfeng on 2016/8/4.
 */
public class XMLUtil {
    private static Logger logger = LoggerFactory.getLogger(XMLUtil.class);

    public static Map<String,String> doXMLParse(String strxml) throws JDOMException, IOException {
        if(null == strxml || "".equals(strxml)) {
            return null;
        }
        Map<String,String> m = new HashMap<String,String>();
        //strxml = new String(strxml.getBytes("UTF-8"));
        SAXBuilder builder = new SAXBuilder();
        //InputStream in = HttpClientUtil.String2Inputstream(strxml);
        //Document doc = builder.build(in);
        Document doc = builder.build(new java.io.ByteArrayInputStream(strxml.getBytes("UTF-8")));
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while(it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if(children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = XMLUtil.getChildrenText(children);
            }
            m.put(k, v);
        }
        //in.close();
        return m;
    }

    public static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if(!children.isEmpty()) {
            Iterator it = children.iterator();
            while(it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if(!list.isEmpty()) {
                    sb.append(XMLUtil.getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }
        return sb.toString();
    }

    public static String getXMLEncoding(String strxml) throws JDOMException, IOException {
        InputStream in = HttpClientUtil.String2Inputstream(strxml);
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        in.close();
        return (String)doc.getProperty("encoding");
    }

    public static void main(String[] args){
        String xml = "<xml>\n" +
                "<return_code><![CDATA[SUCCESS]]></return_code>\n" +
                "<return_msg><![CDATA[Parameter error: The order number field is required and can be up to 28 characters long]]></return_msg>\n" +
                "<result_code><![CDATA[FAIL]]></result_code>\n" +
                "<err_code><![CDATA[PARAM_ERROR]]></err_code>\n" +
                "<err_code_des><![CDATA[Parameter error: The order number field is required and can be up to 28 characters long]]></err_code_des>\n" +
                "<mch_billno><![CDATA[]]></mch_billno>\n" +
                "<mch_id><![CDATA[1389278902]]></mch_id>\n" +
                "<wxappid><![CDATA[wx02b95f7c2c7646ae]]></wxappid>\n" +
                "<re_openid><![CDATA[oT4F0sxw7GEt3PrUIAm1bBleTNLE]]></re_openid>\n" +
                "<total_amount>100</total_amount>\n" +
                "</xml>";
        try {
            //System.out.println(getXMLEncoding(xml));
            //xml = new String(xml.getBytes("UTF-8"));
            Map<String, String> map = XMLUtil.doXMLParse(xml);
            map.size();
        } catch (JDOMException e) {
            logger.error(e.toString(), e);
        } catch (IOException e) {
            logger.error(e.toString(), e);
        }
    }
}
