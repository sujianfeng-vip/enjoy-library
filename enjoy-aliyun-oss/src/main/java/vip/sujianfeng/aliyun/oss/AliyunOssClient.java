package vip.sujianfeng.aliyun.oss;

import vip.sujianfeng.utils.comm.FileHelper;
import vip.sujianfeng.utils.define.CallResult;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.ObjectMetadata;

import java.io.File;
import java.net.URL;

/**
 * @author SuJianFeng
 * @date 2020/4/10 13:52
 **/
public class AliyunOssClient {

    /**
     * 上传本地文件到阿里云oss
     * @param callResult
     * @param key
     * @param fromFile
     */
    public void uploadFile(CallResult<?> callResult, String key, String fromFile){
        //创建OSSClient实例
        OSSClient ossClient = new OSSClient(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
        try {
            if (!FileHelper.isExistFile(fromFile)) {
                callResult.error("not exist file: %s", fromFile);
                return;
            }
            //上传本地文件
            ossClient.putObject(ossConfig.getBacketName(), key, new File(fromFile));
        } catch (Exception e){
            //e.printStackTrace();
            callResult.error("AliyunOssClient.uploadFile" + e);
        }finally {
            //关闭OSSClient
            ossClient.shutdown();
        }
    }

    /**
     * 上传网络文件到阿里云oss
     * @param callResult
     * @param key
     * @param url
     */
    public void uploadFileByUrl(CallResult<?> callResult, String key, String url){
        //创建OSSClient实例
        OSSClient ossClient = new OSSClient(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
        try {
            //上传网络文件
            ossClient.putObject(ossConfig.getBacketName(), key, new URL(url).openStream());
        } catch (Exception e){
            //e.printStackTrace();
            callResult.error("AliyunOssClient.uploadFileByUrl" + e);
        }finally {
            //关闭OSSClient
            ossClient.shutdown();
        }
    }

    public void downloadFile(CallResult<?> callResult, String key, String toFile){
        //创建OSSClient实例
        OSSClient ossClient = new OSSClient(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
        try{
            //上传文件
            ObjectMetadata result = ossClient.getObject(new GetObjectRequest(ossConfig.getBacketName(), key), new File(toFile));
            System.out.println(result);
        } catch (Throwable t) {
            //t.printStackTrace();
            callResult.error("AliyunOssClient.downloadFile" + t);
        } finally {
            //关闭OSSClient
            ossClient.shutdown();
        }
    }

    public AliyunOssClient(AliyunOssConfig ossConfig) {
        this.ossConfig = ossConfig;
    }

    private AliyunOssConfig ossConfig;

    public AliyunOssConfig getOssConfig() {
        return ossConfig;
    }

    public void setOssConfig(AliyunOssConfig ossConfig) {
        this.ossConfig = ossConfig;
    }
}
