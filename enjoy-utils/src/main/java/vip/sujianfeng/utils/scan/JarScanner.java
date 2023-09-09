package vip.sujianfeng.utils.scan;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * author SuJianFeng
 * createTime  2019/8/26 17:24
 **/
public class JarScanner implements Scan {

    @Override
    public List<String> search(ScanFilter filter) {

        List<String> classes = new ArrayList<>();

        try {
            //Obtain an enumeration of URLs by obtaining the class loader through the current thread
            //Enumeration<URL> urlEnumeration = Thread.currentThread().getContextClassLoader().getResources(packageName.replace(".", "/"));
            Enumeration<URL> urlEnumeration = Thread.currentThread().getContextClassLoader().getResources("/");
            while (urlEnumeration.hasMoreElements()) {
                URL url = urlEnumeration.nextElement();//The results obtained are approximately:jar:file:/C:/Users/ibm/.m2/repository/junit/junit/4.12/junit-4.12.jar!/org/junit
                System.out.println("JarScanner -> url.path: " + url.getPath());
                String protocol = url.getProtocol();//Probably jar
                if ("jar".equalsIgnoreCase(protocol)) {
                    //Convert to JarURLConnection
                    JarURLConnection connection = (JarURLConnection) url.openConnection();
                    if (connection != null) {
                        JarFile jarFile = connection.getJarFile();
                        if (jarFile != null) {
                            //Obtain the class entity under the jar file
                            Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
                            while (jarEntryEnumeration.hasMoreElements()) {
                            /*The result of the entry is roughly as follows:
                                    org/
                                    org/junit/
                                    org/junit/rules/
                                    org/junit/runners/*/
                                JarEntry entry = jarEntryEnumeration.nextElement();
                                String jarEntryName = entry.getName();
                                //Here we need to filter classes that are not class files and are not under the basePack package name
                                //jarEntryName.contains(".class") && jarEntryName.replaceAll("/", ".").startsWith(packageName)
                                if (filter == null || filter.test(jarEntryName)){
                                    //String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replace("/", ".");
                                    classes.add(jarEntryName);
                                }
                            }
                        }
                    }
                }else if("file".equalsIgnoreCase(protocol)){
                    //Scan from Maven sub project
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