package vip.sujianfeng.weixin.utils;

import vip.sujianfeng.weixin.model.MyX509TrustManager;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sujianfeng on 2016/8/4.
 */
public class HttpClientUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    public static final String SunX509 = "SunX509";
    public static final String JKS = "JKS";
    public static final String PKCS12 = "PKCS12";
    public static final String TLS = "TLS";

    public static HttpURLConnection getHttpURLConnection(String strUrl)
            throws IOException {
        URL url = new URL(strUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url
                .openConnection();
        return httpURLConnection;
    }

    public static HttpsURLConnection getHttpsURLConnection(String strUrl)
            throws IOException {
        URL url = new URL(strUrl);
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url
                .openConnection();
        return httpsURLConnection;
    }

    public static String getURL(String strUrl) {

        if(null != strUrl) {
            int indexOf = strUrl.indexOf("?");
            if(-1 != indexOf) {
                return strUrl.substring(0, indexOf);
            }

            return strUrl;
        }

        return strUrl;

    }

    public static String getQueryString(String strUrl) {

        if(null != strUrl) {
            int indexOf = strUrl.indexOf("?");
            if(-1 != indexOf) {
                return strUrl.substring(indexOf+1, strUrl.length());
            }

            return "";
        }

        return strUrl;
    }

    public static Map queryString2Map(String queryString) {
        if(null == queryString || "".equals(queryString)) {
            return null;
        }

        Map m = new HashMap();
        String[] strArray = queryString.split("&");
        for(int index = 0; index < strArray.length; index++) {
            String pair = strArray[index];
            HttpClientUtil.putMapByPair(pair, m);
        }

        return m;

    }

    public static void putMapByPair(String pair, Map m) {

        if(null == pair || "".equals(pair)) {
            return;
        }

        int indexOf = pair.indexOf("=");
        if(-1 != indexOf) {
            String k = pair.substring(0, indexOf);
            String v = pair.substring(indexOf+1, pair.length());
            if(null != k && !"".equals(k)) {
                m.put(k, v);
            }
        } else {
            m.put(pair, "");
        }
    }

    public static String bufferedReader2String(BufferedReader reader) throws IOException {
        StringBuffer buf = new StringBuffer();
        String line = null;
        while( (line = reader.readLine()) != null) {
            buf.append(line);
            buf.append("\r\n");
        }

        return buf.toString();
    }

    public static void doOutput(OutputStream out, byte[] data, int len)
            throws IOException {
        int dataLen = data.length;
        int off = 0;
        while(off < dataLen) {
            if(len >= dataLen) {
                out.write(data, off, dataLen);
            } else {
                out.write(data, off, len);
            }
            out.flush();

            off += len;

            dataLen -= len;
        }

    }

    public static SSLContext getSSLContext(
            FileInputStream trustFileInputStream, String trustPasswd,
            FileInputStream keyFileInputStream, String keyPasswd)
            throws NoSuchAlgorithmException, KeyStoreException,
            CertificateException, IOException, UnrecoverableKeyException,
            KeyManagementException {

        // ca
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(HttpClientUtil.SunX509);
        KeyStore trustKeyStore = KeyStore.getInstance(HttpClientUtil.JKS);
        trustKeyStore.load(trustFileInputStream, HttpClientUtil
                .str2CharArray(trustPasswd));
        tmf.init(trustKeyStore);

        final char[] kp = HttpClientUtil.str2CharArray(keyPasswd);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(HttpClientUtil.SunX509);
        KeyStore ks = KeyStore.getInstance(HttpClientUtil.PKCS12);
        ks.load(keyFileInputStream, kp);
        kmf.init(ks, kp);

        SecureRandom rand = new SecureRandom();
        SSLContext ctx = SSLContext.getInstance(HttpClientUtil.TLS);
        ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), rand);

        return ctx;
    }

    public static Certificate getCertificate(File cafile)
            throws CertificateException, IOException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        FileInputStream in = new FileInputStream(cafile);
        Certificate cert = cf.generateCertificate(in);
        in.close();
        return cert;
    }

    public static char[] str2CharArray(String str) {
        if(null == str) return null;

        return str.toCharArray();
    }

    public static void storeCACert(Certificate cert, String alias,
                                   String password, OutputStream out) throws KeyStoreException,
            NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore ks = KeyStore.getInstance("JKS");

        ks.load(null, null);

        ks.setCertificateEntry(alias, cert);

        // store keystore
        ks.store(out, HttpClientUtil.str2CharArray(password));

    }

    public static InputStream String2Inputstream(String str) {
        return new ByteArrayInputStream(str.getBytes());
    }

    public static Map httpRequest(String requestUrl, String requestMethod, String outputStr) {
        StringBuilder buffer = new StringBuilder();
        try {
            // Create an SSLContext object and initialize it using our specified trust manager
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new SecureRandom());
            // Obtain the SSLSocketFactory object from the SSLContext object mentioned above
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // Set request method (GET/POST)
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod))
                httpUrlConn.connect();

            // When there is data to submit
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // Pay attention to encoding format to prevent Chinese garbled characters
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // Convert the returned input stream into a string
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            return JSON.parseObject(buffer.toString(), Map.class);
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return null;
    }



    public static String sendHttpRequest(String urlPath, String encoding, String method) throws Exception {
        if ("GET".equals(method)) {
            return sendHttpRequestGet(urlPath, encoding, method);
        } else {
            return sendHttpRequestPOST(urlPath, encoding, method);
        }
    }

    public static String sendHttpRequestGet(String urlPath, String encoding, String method) throws Exception {
        URL url = new URL(urlPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setConnectTimeout(5 * 1000);
        if (conn.getResponseCode() == 200) {
            InputStream inStream = conn.getInputStream();
            byte[] data = readStream(inStream);
            return new String(data, encoding);
        }
        return "";
    }

    public static byte[] sendHttpRequestGet(String urlPath, String method) throws Exception {
        URL url = new URL(urlPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setConnectTimeout(5 * 1000);
        if (conn.getResponseCode() == 200) {
            InputStream inStream = conn.getInputStream();
            byte[] data = readStream(inStream);
            return data;
        }
        return null;
    }

    public static String sendHttpRequestPOST(String urlPath, String encoding, String method) throws Exception {
        String tempparr[] = urlPath.split("\\?");
        URL url = new URL(tempparr[0]);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod(method);
        conn.setUseCaches(false);
        conn.setConnectTimeout(5 * 1000);
        conn.connect();
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        out.writeBytes(tempparr[1]);
        out.flush();
        out.close();
        InputStream inStream = conn.getInputStream();
        byte[] data = readStream(inStream);
        return new String(data, encoding);
    }

    private static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    public static String getParam(String paramName, String paramValue) {
        return paramName + "=" + paramValue;
    }

}
