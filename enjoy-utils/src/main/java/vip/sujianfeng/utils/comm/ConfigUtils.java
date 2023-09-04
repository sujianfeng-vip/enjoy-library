package vip.sujianfeng.utils.comm;

import vip.sujianfeng.utils.system.SystemEnvUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Author 苏建锋
 * @create 2019-01-13 15:07
 */
public class ConfigUtils {

    private static Logger logger = LoggerFactory.getLogger(ConfigUtils.class);

    private static Map<String, Object> configs = null;
    public static final String ENV_KEY = "spring.profiles.active";
    public static final String ENV_KEY_2 = "SPRING_PROFILES_ACTIVE";
    public static final String ENV_KEY_CLOUD = "spring.cloud.config.profile";

    private static String PROFILE = null;

    private static boolean INITING = false;

    public static void setProfile(String profile){
        if (StringUtilsEx.isNotEmpty(profile)){
            PROFILE = profile;
            logger.info("profile = " + PROFILE);
            initConfigFiles(String.format("application-%s.yml", PROFILE));
            initConfigFiles(String.format("application-%s.properties", PROFILE));
        }
    }

    public static String getProfile(){
        if (StringUtilsEx.isEmpty(PROFILE)){
            PROFILE = getProperty(ENV_KEY);
            if (StringUtilsEx.isEmpty(PROFILE)){
                PROFILE = getProperty(ENV_KEY_2);
            }
            if (StringUtilsEx.isEmpty(PROFILE)){
                PROFILE = getProperty(ENV_KEY_CLOUD);
            }
        }
        return PROFILE;
    }

    public static final String SERVER_PORT_KEY = "server.port";

    public static int getServerPort(){
        int result = ConvertUtils.cInt(getProperty(SERVER_PORT_KEY));
        if (result == 0) {
            result = ConvertUtils.cInt(SystemEnvUtils.getEnv(SERVER_PORT_KEY));
        }
        if (result == 0){
            result = ConvertUtils.cInt(SystemEnvUtils.getCommandParam(SERVER_PORT_KEY));
        }
        if (result == 0){
            result = ConvertUtils.cInt(SystemEnvUtils.getSystemProperty(SERVER_PORT_KEY));
        }
        return ConvertUtils.cInt(result);
    }

    public synchronized static void initConfigs() {
        if (INITING){
            return;
        }
        INITING = true;
        try{
            if (configs != null){
                return;
            }
            getProfile();
            configs = new ConcurrentHashMap<>();
            if (StringUtilsEx.isEmpty(PROFILE)){
                logger.error("profile is null!");
            }else{
                logger.info("profile = " + PROFILE);
            }
            initConfigFiles("bootstrap.yml", "bootstrap.properties");
            if (StringUtilsEx.isEmpty(PROFILE)){
                initConfigFiles("application.yml");
                initConfigFiles("application.properties");
            }else{
                initConfigFiles(String.format("application-%s.properties", PROFILE));
                initConfigFiles(String.format("application-%s.yml", PROFILE));
            }

        }finally {
            INITING = false;
        }
    }

    public static void loadConfigFiles(String... files){
        initConfigs();
        initConfigFiles(files);
    }

    public static void initConfigFiles(String... files){
        for (String file : files) {
            Object obj = null;
            if (file.toLowerCase().endsWith(".yml")){
                obj = getYml(file);
            }
            if (file.toLowerCase().endsWith(".properties")){
                obj = getProperties(file);
            }
            if (obj != null && configs != null) configs.put(file, obj);
        }
    }

    public static Map getAllProperties(){
        Map result = new HashMap();
        initConfigs();
        if (configs == null){
            return result;
        }
        for (Map.Entry<String, Object> entry : configs.entrySet()) {
            if (entry.getValue() instanceof Map){
                flatten("", entry.getValue(), result);
            }
            if (entry.getValue() instanceof Properties){
                Properties properties = (Properties) entry.getValue();
                Set<String> propertyNames = properties.stringPropertyNames();
                if (propertyNames != null){
                    for (String propertyName : propertyNames) {
                        result.put(propertyName, properties.get(properties));
                    }
                }
            }
        }
        return result;
    }

    public static Properties getProperties(String propertiesFileName) {
        Properties properties = new Properties();
        try {
            InputStream inStream = ConfigUtils.class.getClassLoader().getResourceAsStream(propertiesFileName);
            if (inStream != null){
                logger.info("\n==============\nCustom profile loaded successfully: %s\n==============\n", propertiesFileName);
            }else{
                return null;
            }
            properties.load(inStream);
        } catch (IOException e) {
            logger.error(e.toString(), e);
            return null;
        }
        return properties;
    }

    public static String getProperty(String propertiesFileName, String key) {
        Properties properties = getProperties(propertiesFileName);
        if (properties == null){
            return "";
        }
        return properties.getProperty(key);
    }

    /**
     * 获取配置文件值，包含properties和yml资源文件
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        initConfigs();
        String result = "";
        if (configs != null){
            for (Map.Entry<String, Object> entry : configs.entrySet()) {
                Object obj = entry.getValue();
                if (obj instanceof Properties){
                    Properties properties = (Properties) obj;
                    Object value = properties.get(key);
                    if (value != null){
                        result = ConvertUtils.cStr(value);
                        break;
                    }
                }
                if (obj instanceof Map){
                    Map map = (Map) obj;
                    Object value = getYmlKey(map, key);
                    if (value != null){
                        result = ConvertUtils.cStr(value);
                        break;
                    }
                }
            }
        }
        if (StringUtilsEx.isEmpty(result)){
            result = SystemEnvUtils.getEnv(key);
        }
        if (StringUtilsEx.isEmpty(result)){
            result = SystemEnvUtils.getCommandParam(key);
        }
        if (StringUtilsEx.isEmpty(result)){
            result = SystemEnvUtils.getSystemProperty(key);
        }
        return result;
    }

    public static int getPropertyInt(String key) {
        return ConvertUtils.cInt(getProperty(key));
    }


    public static Map<String, Object> getYml(String ymlFile){
        Map<String, Object> result = null;
        try {
            Yaml yaml = new Yaml();
            if (FileHelper.isExistFile(ymlFile)){
                result = yaml.load(new FileInputStream(ymlFile));
            }
            if (result == null){
                InputStream inputStream = ConfigUtils.class.getClassLoader().getResourceAsStream(ymlFile);
                if (inputStream != null) {
                    result = yaml.load(inputStream);
                }
            }
            if (result == null){
                URL url = ConfigUtils.class.getClassLoader().getResource(ymlFile);
                if (url != null) {
                    result = yaml.load(new FileInputStream(url.getFile()));
                }
            }
            if (result != null){
                logger.info("\n==============\nCustom profile loaded successfully :{}\n==============\n", ymlFile);
            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return result;
    }

    /**
     * 加载jar包或class目录下资源文件，并转换为字符串
     * @param fileName
     * @return
     */
    public static String loadResFile(String fileName){
        InputStream inputStream = ConfigUtils.class.getResourceAsStream(fileName);
        return inputStream != null ? new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining(System.lineSeparator())): "";
    }

    public static Object getYmlKey(String ymlFile, String key){
        Map ymlMap = getYml(ymlFile);
        return getYmlKey(ymlMap, key);
    }

    /**
     * 将多级树形结构的map压扁为一级
     * @return
     */
    public static void flatten(String parentKey, Object src, Map desc){
        if (src instanceof Map){
            Map map = (Map) src;
            Set set = map.keySet();
            for (Object key : set) {
                Object obj = map.get(key);
                String fullKey = StringUtilsEx.isNotEmpty(parentKey) ? String.format("%s.%s", parentKey, key) : ConvertUtils.cStr(key);
                flatten(fullKey, obj, desc);
            }
            return;
        }
        desc.put(parentKey, src);
    }

    public static Object getYmlKey(Map ymlMap, String key){
        Map map = new HashMap();
        flatten("", ymlMap, map);
        return map.get(key);
    }

    /**
     * 设置环境变量
     * @param name
     * @param value
     */
    public static void setEnv(String name, String value){
        SystemEnvUtils.setEnv(name, value);
    }

}
