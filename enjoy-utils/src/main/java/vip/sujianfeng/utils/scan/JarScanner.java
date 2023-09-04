package vip.sujianfeng.utils.scan;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author SuJianFeng
 * @date 2019/8/26 17:24
 **/
public class JarScanner implements Scan {

    @Override
    public List<String> search(ScanFilter filter) {

        List<String> classes = new ArrayList<>();

        try {
            //通过当前线程得到类加载器从而得到URL的枚举
            //Enumeration<URL> urlEnumeration = Thread.currentThread().getContextClassLoader().getResources(packageName.replace(".", "/"));
            Enumeration<URL> urlEnumeration = Thread.currentThread().getContextClassLoader().getResources("/");
            while (urlEnumeration.hasMoreElements()) {
                URL url = urlEnumeration.nextElement();//得到的结果大概是：jar:file:/C:/Users/ibm/.m2/repository/junit/junit/4.12/junit-4.12.jar!/org/junit
                System.out.println("JarScanner -> url.path: " + url.getPath());
                String protocol = url.getProtocol();//大概是jar
                if ("jar".equalsIgnoreCase(protocol)) {
                    //转换为JarURLConnection
                    JarURLConnection connection = (JarURLConnection) url.openConnection();
                    if (connection != null) {
                        JarFile jarFile = connection.getJarFile();
                        if (jarFile != null) {
                            //得到该jar文件下面的类实体
                            Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
                            while (jarEntryEnumeration.hasMoreElements()) {
                            /*entry的结果大概是这样：
                                    org/
                                    org/junit/
                                    org/junit/rules/
                                    org/junit/runners/*/
                                JarEntry entry = jarEntryEnumeration.nextElement();
                                String jarEntryName = entry.getName();
                                //这里我们需要过滤不是class文件和不在basePack包名下的类
                                //jarEntryName.contains(".class") && jarEntryName.replaceAll("/", ".").startsWith(packageName)
                                if (filter == null || filter.test(jarEntryName)){
                                    //String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replace("/", ".");
                                    classes.add(jarEntryName);
                                }
                            }
                        }
                    }
                }else if("file".equalsIgnoreCase(protocol)){
                    //从maven子项目中扫描
                    FileScanner fileScanner = new FileScanner(url.getPath());
                    classes.addAll(fileScanner.search(filter));
                }
            }
        }catch (IOException e){
            throw new RuntimeException(e.getMessage(), e);
        }
        return classes;
    }
}