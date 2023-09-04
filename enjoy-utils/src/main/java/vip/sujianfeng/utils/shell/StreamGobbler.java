package vip.sujianfeng.utils.shell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.sujianfeng.utils.comm.StringBuilderEx;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author SuJianFeng
 * @Date 2019/2/15 9:36
 * 用于处理Runtime.getRuntime().exec产生的错误流及输出流
 **/
public class StreamGobbler extends Thread {
    private static Logger logger = LoggerFactory.getLogger(StreamGobbler.class);
    private InputStream is;
    private String type;
    private OutputStream os;
    private CopyOnWriteArrayList<String> logList = new CopyOnWriteArrayList<>();
    private boolean completed = false;
    private String logCharset;

    public CopyOnWriteArrayList<String> getLogList() {
        return logList;
    }

    /**
     * 取出信息并清空
     * @return
     */
    public List<String> pickLog() {
        List<String> list = new ArrayList<>();
        CopyOnWriteArrayList<String> logList = this.logList;
        while (logList.size() > 0) {
            list.add(logList.get(0));
            logList.remove(0);
        }
        return list;
    }

    public String getLog(){
        StringBuilderEx result = new StringBuilderEx();
        List<String> list = pickLog();
        list.forEach(result::appendRow);
        return result.toString();
    }

    public StreamGobbler(InputStream is, String type, String logCharset) {
        this(is, type, null, logCharset);
    }

    public StreamGobbler(InputStream is, String type, OutputStream redirect, String logCharset) {
        this.logCharset = logCharset;
        this.is = is;
        this.type = type;
        this.os = redirect;
    }

    @Override
    public void run() {
        InputStreamReader isr = null;
        BufferedReader br = null;
        PrintWriter pw = null;
        try {
            if (os != null)
                pw = new PrintWriter(os);
            isr = new InputStreamReader(is, logCharset);
            br = new BufferedReader(isr);
            String line;
            while ( (line = br.readLine()) != null) {
                if (pw != null)
                    pw.println(this.type + " > " + line);
                logList.add(line);
            }
            if (pw != null){
                pw.flush();
                pw.close();
            }
            br.close();
            isr.close();
        } catch (IOException e) {
            logger.error(e.toString(), e);
        } finally {
            completed = true;
        }
    }

    public boolean isCompleted() {
        return completed;
    }


    public String getLogCharset() {
        return logCharset;
    }

    public void setLogCharset(String logCharset) {
        this.logCharset = logCharset;
    }
}
