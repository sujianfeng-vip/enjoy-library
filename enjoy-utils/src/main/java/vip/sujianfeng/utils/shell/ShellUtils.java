package vip.sujianfeng.utils.shell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.sujianfeng.utils.define.CallResult;

import java.io.File;

/**
 * author SuJianFeng
 * createTime  2019/7/30 17:30
 * Shell Script Running Tool Class
 **/
public class ShellUtils {

    private static Logger logger = LoggerFactory.getLogger(ShellUtils.class);
    public static void exec(CallResult<?> opResult, String var1, String... vars){
        exec(opResult, 1000, var1, vars);
    }

    public static void exec(CallResult<?> opResult, int timeOut, String var1, String... vars){
        exec(opResult, timeOut, var1, vars, null);
    }

    public static void exec(CallResult<?> opResult, int timeOut, String var1, String[] vars, File file){
        new ShellExec().exec(opResult, timeOut, var1, vars, file);
    }
}
