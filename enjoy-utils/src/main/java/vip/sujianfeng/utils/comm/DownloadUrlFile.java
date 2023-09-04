package vip.sujianfeng.utils.comm;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.sujianfeng.utils.intf.CommEvent;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 下载网络文件到本地文件
 * Created by pilot on 2017/7/1.
 */
public class DownloadUrlFile {
    private static Logger logger = LoggerFactory.getLogger(DownloadUrlFile.class);

    public static class DownLoadProcess {
        private String message;
        private boolean breakRun = false;
        private long totalSize;
        private long currSize;

        public long getTotalSize() {
            return totalSize;
        }

        public void setTotalSize(long totalSize) {
            this.totalSize = totalSize;
        }

        public long getCurrSize() {
            return currSize;
        }

        public void setCurrSize(int currSize) {
            this.currSize = currSize;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isBreakRun() {
            return breakRun;
        }

        public void setBreakRun(boolean breakRun) {
            this.breakRun = breakRun;
        }
    }

    /**
     * 从网络Url中下载文件
     * @param urlStr
     * @param fileName
     * @param savePath
     * @param processEvent 下载进度回调
     * @throws IOException
     */
    public static void downLoadFromUrl(String urlStr, String fileName, String savePath, int timeOutSecond,  CommEvent<DownLoadProcess> processEvent, CommEvent<Long> completed) throws IOException{
        if (!urlStr.contains("http")){
            urlStr = "http://" + urlStr;
        }
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(timeOutSecond * 1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //文件保存位置
        File saveDir = new File(savePath);
        if(!saveDir.exists()){
            saveDir.mkdir();
        }
        long len = conn.getContentLengthLong();
        inputStreamSaveToFile(inputStream, len, saveDir + File.separator + fileName, processEvent);
        inputStream.close();
        if (completed != null) {
            completed.call(len);
        }
    }

    public static void downLoadFromUrl(String urlStr, String fileName, String savePath, int timeOutSecond) throws IOException{
        downLoadFromUrl(urlStr, fileName, savePath, timeOutSecond, null, null);
    }


    /**
     * 读取输入流并写入文件中
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static void inputStreamSaveToFile(InputStream inputStream, long contentLength, String fileName, CommEvent<DownLoadProcess> processEvent) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        FileOutputStream fos = new FileOutputStream(fileName);
        try {
            DownLoadProcess p = new DownLoadProcess();
            p.message = "正在下载....";
            p.totalSize = contentLength;
            p.currSize = 0;
            while((len = inputStream.read(buffer)) != -1) {
                fos.write( buffer, 0, len );
                p.currSize += len;
                if (processEvent != null) {
                    processEvent.call(p);
                }
                //通过外部控制，取消下载
                if (p.breakRun) {
                    break;
                }
            }
        }finally {
            fos.close();
        }
    }

    public static void main(String[] args) {
        try {
            String url = "https://appraise-a.oss-cn-hangzhou.aliyuncs.com/20220802_161105.mp4";
            // String url = "https://hougu.oss-cn-hangzhou.aliyuncs.com/fkk/Chanjet.zip";
            downLoadFromUrl(url,"1.mp4","d:/temp/", 3, (process)-> System.out.printf("进度: %s\n", JSON.toJSONString(process)), (process)-> System.out.printf("下载完成: %s\n", process));
        }catch (Exception e) {
            logger.error(e.toString(), e);
        }
    }
}
