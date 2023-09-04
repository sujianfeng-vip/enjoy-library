package vip.sujianfeng.utils.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @Author SuJianFeng
 * @Date 2019/2/5 19:25
 * 系统环境变量相关
 **/
public class SystemEnvUtils {
    private static Logger logger = LoggerFactory.getLogger(SystemEnvUtils.class);
    /**
     * 环境变量
     * @return
     */
    public static Map<String, String> getSystemEnv(){
        return System.getenv();
    }
    public static String getEnv(String param){
        return System.getenv().get(param);
    }

    /**
     * 系统属性
     * @return
     */
    public static Properties getSystemProperties(){
        return System.getProperties();
    }

    public static String getSystemProperty(String key){
        return System.getProperty(key);
    }

    /**
     * 堆内存信息
     * @return
     */
    public static MemoryMXBean getMemoryMXBean(){
        MemoryMXBean memorymbean = ManagementFactory.getMemoryMXBean();
        /*System.out.println("堆内存信息: " + memorymbean.getHeapMemoryUsage());
        System.out.println("方法区内存信息: " + memorymbean.getNonHeapMemoryUsage());*/
        return memorymbean;
    }

    /**
     * jvm运行参数
     * @return
     */
    public static List<String> getJvmParamList(){
        List<String> inputArgs = ManagementFactory.getRuntimeMXBean().getInputArguments();
        /*System.out.println("\n#####################运行时设置的JVM参数#######################");
        System.out.println(inputArgs);*/
        return inputArgs;
    }

    /**
     * 取得jar包运行参数值
     * @param paramName
     * @return
     */
    public static String getCommandParam(String paramName){
        String cmd = getSystemProperty("sun.java.command");
        //System.out.println("====>sun.java.command: " + cmd);
        String[] cmdArrs = cmd.split(" ");
        for (String cmdArr : cmdArrs) {
            if (cmdArr.startsWith(paramName) || cmdArr.startsWith("--" + paramName)){
                String[] arr = cmdArr.split("=");
                if (arr.length > 1){
                    return arr[1];
                }
            }
        }
        return "";
    }

    /**
     * 运行是状态信息，如：内存使用情况
     * @return
     */
    public static Runtime getSystemRuntime(){
        Runtime runtime = Runtime.getRuntime();
        /*
        System.out.println("\n#####################运行时内存情况#######################");
        long totle = runtime.totalMemory();
        System.out.println("总的内存量 [" + totle + "]");
        long free = runtime.freeMemory();
        System.out.println("空闲的内存量 [" + free + "]");
        long max = runtime.maxMemory();
        System.out.println("最大的内存量 [" + max + "]");
        */
        return runtime;
    }

    /**
     * 设置环境变量
     * @param name
     * @param value
     */
    public static void setEnv(String name, String value) {
        try {
            Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
            Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
            theEnvironmentField.setAccessible(true);
            Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
            env.put(name, value);
            Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
            theCaseInsensitiveEnvironmentField.setAccessible(true);
            Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
            cienv.put(name, value);
        } catch (NoSuchFieldException e) {
            Class[] classes = Collections.class.getDeclaredClasses();
            Map<String, String> env = System.getenv();
            for(Class cl : classes) {
                if("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {

                    try {
                        Field field = cl.getDeclaredField("m");
                        field.setAccessible(true);
                        Object obj = field.get(env);
                        Map<String, String> map = (Map<String, String>) obj;
                        map.clear();
                        map.put(name, value);
                    } catch (NoSuchFieldException | IllegalAccessException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException e) {
            logger.error(e.toString(), e);
        }
    }

    private static Map<String, String> CURR_ENV_MAP = new HashMap<>();

    /**
     * 判断环境变量是否发生变化
     * @return
     */
    public static boolean systemEnvIsModified(){
        boolean modified = false;
        boolean isInit = CURR_ENV_MAP.size() == 0;
        Map<String, String> envMap = System.getenv();
        Map<String, String> tmp = new HashMap<>();
        for (Map.Entry<String, String> entry : envMap.entrySet()) {
            tmp.put(entry.getKey(), entry.getValue());
        }
        if (envMap.size() != CURR_ENV_MAP.size()){
            modified = true;
        }
        if (!modified){
            for (Map.Entry<String, String> entry : envMap.entrySet()) {
                String value = CURR_ENV_MAP.get(entry.getKey());
                if (value == null || !value.equals(entry.getValue())){
                    modified = true;
                    break;
                }
            }
        }
        if (isInit){
            CURR_ENV_MAP = tmp;
            return false;
        }
        if (modified){
            CURR_ENV_MAP = tmp;
        }
        return modified;
    }

    static {
        systemEnvIsModified();
    }
}
