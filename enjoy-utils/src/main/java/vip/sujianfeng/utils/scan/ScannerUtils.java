package vip.sujianfeng.utils.scan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.sujianfeng.utils.comm.ConfigUtils;
import vip.sujianfeng.utils.comm.StringUtilsEx;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SuJianFeng
 * @date 2019/8/26 17:26
 * 类包扫描工具类
 **/
public class ScannerUtils {

    public static String[] getInitScanPath(Class<?> fromCls){
        URL url = fromCls.getResource("/");
        if (url == null) url = fromCls.getResource("");
        if (url == null) return new String[]{ ConfigUtils.getProperty("user.dir") };
        String rootPath = url.getPath();
        if (rootPath.toLowerCase().contains("classes")){
            rootPath = StringUtilsEx.leftStr(rootPath, "classes");
        }
        if (rootPath.toLowerCase().contains(".jar")){
            String libsPath = rootPath;
            libsPath = StringUtilsEx.leftStrEx(libsPath, ".jar");
            libsPath = StringUtilsEx.leftStrEx(libsPath, "/");
            return new String[]{rootPath, libsPath + "/libs/"};
        }
        return new String[]{rootPath};
    }

    public static List<Class<?>> searchClass(String backPackage, ScanFilter filter, Class<?> fromCls){
        return searchClass(backPackage, filter, getInitScanPath(fromCls));
    }

    public static List<Class<?>> searchClass(String backPackage, ScanFilter filter, String... scanPaths){
        List<Class<?>> result = new ArrayList<>();
        ScanFilter scanFilter = name -> {
            if (StringUtilsEx.isEmpty(name)){
                return false;
            }
            if (filter != null && !filter.test(name)){
                return false;
            }
            if (!name.toLowerCase().endsWith(".class")){
                return false;
            }
            if (name.contains("$")){
                return false;
            }
            name = name.replace("/", ".");
            name = name.replace("\\", ".");
            return name.contains(backPackage);
        };

        List<String> search = search(scanFilter, scanPaths);
        logger.info("共扫描到{}个类，正在加载初始化....", search.size());
        for (String name : search) {
            name = name.replace("/", ".");
            name = name.replace("\\", ".");
            name = name.replace(".class", "");
            String clsName = backPackage + StringUtilsEx.rightStr(name, backPackage);
            try {
                Class<?> cls = Class.forName(clsName);
                result.add(cls);
            } catch (ClassNotFoundException e) {
                logger.error("not found: {}",  clsName);
            }
        }
        logger.info("共扫描到{}个类，初始化完成!", search.size());
        return result;
    }

    public static List<String> search(ScanFilter filter , Class<?> fromCls){
        return search(filter, getInitScanPath(fromCls));
    }

    public static List<String> search(ScanFilter filter, String... scanPaths){
        StringBuilder tmp = new StringBuilder();
        for (String scanPath : scanPaths) {
            if (tmp.length() > 0) tmp.append(",");
            tmp.append(scanPath);
        }
        return ScanExecutor.getInstance(scanPaths).search(filter);
    }

    public static boolean isAssignableFrom(Class<?> aClass, Class<?> parent){
        if (aClass == null) {
            return false;
        }
        if (parent.isAssignableFrom(aClass)){
            return true;
        }
        return isAssignableFrom(aClass.getSuperclass(), parent);
    }

    private static Logger logger = LoggerFactory.getLogger(ScannerUtils.class);
}
