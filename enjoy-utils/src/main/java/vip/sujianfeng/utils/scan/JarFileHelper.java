package vip.sujianfeng.utils.scan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.sujianfeng.utils.comm.StringUtilsEx;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

public class JarFileHelper {

    private static Logger logger = LoggerFactory.getLogger(JarFileHelper.class);
	public static String getResource(String fileName) throws IOException {
		InputStream is = JarFileHelper.class.getClassLoader().getResourceAsStream(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(is,
				"UTF-8"));
		StringBuilder sb = new StringBuilder();
		String s = "";
		while ((s = br.readLine()) != null) {
			sb.append(s).append("\n");
		}
		return sb.toString();
	}

    /**
     * 取得指定jar包内部所有文件
     * @param jarFile
     * @return
     * @throws IOException
     */
	public static List<String> getJarFileList(String jarFile, ScanFilter filter) {
        List<String> result = new ArrayList<>();
        if (StringUtilsEx.isEmpty(jarFile)){
            return result;
        }
        //jarFile = StringUtilsEx.rightStr(jarFile, "file:/");
        jarFile = StringUtilsEx.rightStr(jarFile, "file:");
        jarFile = StringUtilsEx.leftStr(jarFile, ".jar") + ".jar";
        File file = new File(jarFile);
        JarFile localJarFile = null;
        try {
            localJarFile = new JarFile(file);
        } catch (IOException e) {
            logger.error(e.toString(), e);
            return result;
        }
        System.out.println("scan jar: " + file);
        Enumeration<JarEntry> entries = localJarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String fileName = jarEntry.getName();
            if (filter == null || filter.test(fileName)){
                result.add(fileName);
            }
            if (fileName.toLowerCase().contains(".jar")){
                List<String> tmpList = getJarInJarFileList(jarFile, fileName, filter);
                result.addAll(tmpList);
            }
        }
        return result;
    }

    /**
     * 取得jar包内的jar包文件列表
     * @param jarFile
     * @param insideJarFile
     * @return
     */
    public static List<String> getJarInJarFileList(String jarFile, String insideJarFile, ScanFilter filter) {
        List<String> result = new ArrayList<>();
        URL url = null;
        try {
            String file = String.format("%s!/%s", jarFile, insideJarFile);
            if (!jarFile.toLowerCase().startsWith("file:")){
                file = "file:" + file;
            }
            url = new URL("jar", null, 0, file);
            System.out.println("scan jar: " + file);
            URLConnection con = url.openConnection();
            if (con instanceof JarURLConnection) {
                JarURLConnection conn = (JarURLConnection) con;
                JarInputStream jarInputStream = new JarInputStream(conn.getInputStream());
                JarEntry entry;
                while ((entry = jarInputStream.getNextJarEntry()) != null) {
                    if (filter == null || filter.test(entry.getName())){
                        result.add(entry.getName());
                    }
                }
            }
        } catch (IOException e) {
            logger.error(e.toString(), e);
        }
        return result;
    }


	/**
	 * 加载类路径下的文件
	 *
	 * @param filePath E.g. "com/qfmis/model/config/datasource/com/ds-com-person.xml"
	 * @return
	 */
	public static InputStream getResoutce(String filePath) {
		return JarFileHelper.class.getClassLoader().getResourceAsStream(filePath);
	}

	public static void main(String[] args) {
		String file = "/com/qfmis/model/config/datasource/com/ds-com-person.xml";
		try {
			String s = getResource(file);
			System.out.println(s);

		} catch (IOException e) {
			logger.error(e.toString(), e);
		}
	}

}
