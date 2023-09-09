package vip.sujianfeng.utils.shell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.sujianfeng.utils.comm.StringBuilderEx;
import vip.sujianfeng.utils.define.CallResult;

import java.io.File;

/**
 * author SuJianFeng
 * createTime  2019/7/30 17:30
 * shell脚本运行
 **/
public class ShellExec {
    private static Logger logger = LoggerFactory.getLogger(ShellExec.class);

    private String logChatset = "UTF-8";

    public void exec(CallResult<?> opResult, String var1, String... vars){
        this.exec(opResult, 1000, var1, vars);

    }

    public void exec(CallResult<?> opResult, int timeOut, String var1, String... vars){
        this.exec(opResult, timeOut, var1, vars, null);
    }

    public void exec(CallResult<?> opResult, int timeOut, String var1, String[] vars, File file){
        try{
            Process p;
            if(var1 != null && vars != null && file != null){
                p = Runtime.getRuntime().exec(var1, vars, file);
            }else if(var1 != null && vars != null){
                p = Runtime.getRuntime().exec(var1, vars);
            }else if(var1 != null){
                p = Runtime.getRuntime().exec(var1);
            }else{
                p = Runtime.getRuntime().exec(vars);
            }
            StringBuilderEx params = new StringBuilderEx();
            if (vars.length > 0){
                for (int i = 0; i < vars.length; i++) {
                    if (params.length() > 0) params.append(",");
                    params.append(vars[i]);
                }
            }
            long beginTime = System.currentTimeMillis();
            logger.info("\n======ShellUtils.exec====>\ncmd: {}\nparams: {}\ntimeOut:{}...\nfile: {}\n==============\n",
                    var1, params, timeOut, file != null ? file.getAbsolutePath() : "");
            StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR", logChatset);
            errorGobbler.start();
            StreamGobbler outGobbler = new StreamGobbler(p.getInputStream(), "STDOUT", logChatset);
            outGobbler.start();
            int exitValue = p.waitFor(); //0-运行正常，非0运行异常
            while (!(outGobbler.isCompleted() && errorGobbler.isCompleted()) && ((System.currentTimeMillis() - beginTime) < timeOut)){
                Thread.sleep(100);
            }
            opResult.getAttributes().put("errorLog", errorGobbler.getLog());
            opResult.getAttributes().put("infoLog", outGobbler.getLog());
            if (exitValue != 0){
                opResult.error("error:%s|info:%s", errorGobbler.getLog(), outGobbler.getLog());
            }
        }catch (Exception e){
            logger.error(e.toString(), e);
            opResult.error(e.toString());
        }
    }


    public String getLogChatset() {
        return logChatset;
    }

    public void setLogChatset(String logChatset) {
        this.logChatset = logChatset;
    }
}
