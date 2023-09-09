package vip.sujianfeng.utils.comm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * author SuJianFeng
 * createTime  2019/8/23 9:58
 **/
public class AddressUtils {
    private static Logger logger = LoggerFactory.getLogger(AddressUtils.class);
    public static String getCurrIp(){
        try {
            return new AddressUtils().getInnetIp();
        } catch (SocketException e) {
            logger.error(e.toString(), e);
        }
        return "";
    }

    public String getInnetIp() throws SocketException {
        String localip = null;// 本地IP，如果没有配置外网IP则返回它
        String netip = null;// 外网IP
        Enumeration<NetworkInterface> netInterfaces;
        netInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        boolean finded = false;// 是否找到外网IP
        while (netInterfaces.hasMoreElements() && !finded) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> address = ni.getInetAddresses();
            while (address.hasMoreElements()) {
                ip = address.nextElement();
                if (!ip.isSiteLocalAddress()
                        && !ip.isLoopbackAddress()
                        && !ip.getHostAddress().contains(":")) {// 外网IP
                    netip = ip.getHostAddress();
                    finded = true;
                    break;
                } else if (ip.isSiteLocalAddress()
                        && !ip.isLoopbackAddress()
                        && !ip.getHostAddress().contains(":")) {// 内网IP
                    localip = ip.getHostAddress();
                }
            }
        }
        if (netip != null && !"".equals(netip)) {
            return netip;
        } else {
            return localip;
        }
    }

    public String getV4IP(){
        String ip = "";
        String chinaz = "http://ip.chinaz.com";

        StringBuilder inputLine = new StringBuilder();
        String read = "";
        URL url = null;
        HttpURLConnection urlConnection = null;
        BufferedReader in = null;
        try {
            url = new URL(chinaz);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedReader( new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
            while((read=in.readLine())!=null){
                inputLine.append(read+"\r\n");
            }
        //System.out.println(inputLine.toString());
        } catch (MalformedURLException e) {
            logger.error(e.toString(), e);
        } catch (IOException e) {
            logger.error(e.toString(), e);
        }finally{
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    logger.error(e.toString(), e);
                }
            }
        }
        Pattern p = Pattern.compile("\\<dd class\\=\"fz24\">(.*?)\\<\\/dd>");
        Matcher m = p.matcher(inputLine.toString());
        if(m.find()){
            String ipstr = m.group(1);
            ip = ipstr;
            //System.out.println(ipstr);
        }
        return ip;
    }



    public String getAddresses(String content, String encoding) throws UnsupportedEncodingException {
        String urlStr = "http://ip.taobao.com/service/getIpInfo.php";
        //http://whois.pconline.com.cn
        String returnStr = this.getResult(urlStr, content, encoding);
        if (returnStr != null) {
            String[] temp = returnStr.split(",");
            if (temp.length < 3) {
                return "0";
            }

            String country = "";
            String area = "";
            String region = "";
            String city = "";
            String county = "";
            String isp = "";
            for (int i = 0; i < temp.length; i++) {
                switch (i) {
                    case 2:
                        country = (temp[i].split(":"))[1].replaceAll("\"", "");
                        country = URLDecoder.decode(country, encoding);// 国家
                        break;
                    case 3:
                        area = (temp[i].split(":"))[1].replaceAll("\"", "");
                        area = URLDecoder.decode(area, encoding);// 地区
                        break;
                    case 4:
                        region = (temp[i].split(":"))[1].replaceAll("\"", "");
                        region = URLDecoder.decode(region, encoding);// 省份
                        break;
                    case 5:
                        city = (temp[i].split(":"))[1].replaceAll("\"", "");
                        city = URLDecoder.decode(city, encoding);// 市区
                        break;
                    case 6:
                        county = (temp[i].split(":"))[1].replaceAll("\"", "");
                        county = URLDecoder.decode(county, encoding);// 地区
                        break;
                    case 7:
                        isp = (temp[i].split(":"))[1].replaceAll("\"", "");
                        isp = URLDecoder.decode(isp, encoding); // ISP公司
                        break;
                }
            }


            return new StringBuffer("adddress:"+country+",").append(region + ",").append(city + ",").append(county+",").append("ISP:"+isp)
                    .toString();
        }
        return null;
    }


    private String getResult(String urlStr, String content, String encoding) {
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(33000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.connect();
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(content);
            out.flush();
            out.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            return buffer.toString();
        } catch (IOException e) {
            logger.error(e.toString(), e);
        } finally {
            if (connection != null) {
                connection.disconnect();// 关闭连接
            }
        }
        return null;
    }

    public static void main(String[] args) {
        AddressUtils addressUtils = new AddressUtils();
        String ip1="";
        try {
            ip1 = addressUtils.getInnetIp();
        } catch (SocketException e1) {
            e1.printStackTrace();
        }
        String ip2 = addressUtils.getV4IP();
        String address = "";
        try {
            address = addressUtils.getAddresses("ip=" + ip2, "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.toString(), e);
        }
        System.out.println("adddress => " + address);
        System.out.println("******************************");
        System.out.println("Please enter the IP address you want to query (enter exit to exit):");
        Scanner scan=new Scanner(System.in);
        String ip="";
        while(!"exit".equals(ip=scan.next())) {
            try {
                address = addressUtils.getAddresses("ip=" + ip, "utf-8");
            } catch (UnsupportedEncodingException e) {
                logger.error(e.toString(), e);
            }
            System.out.println(ip+"="+address);
            System.out.println("******************************");
            System.out.println("Please enter the IP address you want to query (enter exit to exit):");
        }
        scan.close();
        System.out.println("goodbye");
    }

}
