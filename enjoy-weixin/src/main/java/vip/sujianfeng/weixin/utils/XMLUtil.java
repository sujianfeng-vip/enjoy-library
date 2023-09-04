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
    /**
     * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
     * @param  strxml
     * @return
     * @throws  JDOMException
     * @throws IOException
     */
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
        //关闭流
        //in.close();
        return m;
    }

    /**
     * 获取子结点的xml
     * @param children
     * @return String
     */
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
    /**
     * 获取xml编码字符集
     * @param strxml
     * @return
     * @throws IOException
     * @throws JDOMException
     */
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
                "<return_msg><![CDATA[参数错误:订单号字段必填,最长为28个字符.]]></return_msg>\n" +
                "<result_code><![CDATA[FAIL]]></result_code>\n" +
                "<err_code><![CDATA[PARAM_ERROR]]></err_code>\n" +
                "<err_code_des><![CDATA[参数错误:订单号字段必填,最长为28个字符.]]></err_code_des>\n" +
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
