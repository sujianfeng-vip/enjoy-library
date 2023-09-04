package vip.sujianfeng.utils.shell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @Author SuJianFeng
 * @Date 2019/2/15 9:36
 * 用于处理Runtime.getRuntime().exec产生的错误流及输出流
 **/
public class ShellExecInteractStream extends Thread {

    public static final String ERROR = "ERROR";
    public static final String STDOUT = "STDOUT";

    private static Logger logger = LoggerFactory.getLogger(ShellExecInteractStream.class);
    private ShellExecInteract owner;
    private InputStream is;
    private String type;
    private boolean completed = false;
    private String logCharset;
    private CallBack callBack;

    public interface CallBack {
        void call(ShellExecInteract owner, String type, String line);
    }

    public ShellExecInteractStream(ShellExecInteract owner, InputStream is, CallBack callBack, String type, String logCharset) {
        this.owner = owner;
        this.callBack = callBack;
        this.logCharset = logCharset;
        this.is = is;
        this.type = type;
    }

    @Override
    public void run() {
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            isr = new InputStreamReader(is, logCharset);
            br = new BufferedReader(isr);
            String line;
            while ( (line = br.readLine()) != null) {
                this.callBack.call(owner, type, line);
            }
        } catch (IOException e) {
            logger.error(e.toString(), e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (isr != null) {
                    isr.close();
                }
            } catch (IOException e) {
                logger.error(e.toString(), e);
            }
            completed = true;
        }
    }

    public boolean isCompleted() {
        return completed;
    }
}
