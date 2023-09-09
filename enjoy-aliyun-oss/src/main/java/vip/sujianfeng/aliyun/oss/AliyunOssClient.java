package vip.sujianfeng.aliyun.oss;

import vip.sujianfeng.utils.comm.FileHelper;
import vip.sujianfeng.utils.define.CallResult;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.ObjectMetadata;

import java.io.File;
import java.net.URL;

/**
 * author SuJianFeng
 * createTime  2020/4/10 13:52
 **/
public class AliyunOssClient {

    public void uploadFile(CallResult<?> callResult, String key, String fromFile){
        OSSClient ossClient = new OSSClient(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
        try {
            if (!FileHelper.isExistFile(fromFile)) {
                callResult.error("not exist file: %s", fromFile);
                return;
            }
            ossClient.putObject(ossConfig.getBacketName(), key, new File(fromFile));
        } catch (Exception e){
            callResult.error("AliyunOssClient.uploadFile" + e);
        }finally {
            ossClient.shutdown();
        }
    }

    public void uploadFileByUrl(CallResult<?> callResult, String key, String url){
        OSSClient ossClient = new OSSClient(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
        try {
            ossClient.putObject(ossConfig.getBacketName(), key, new URL(url).openStream());
        } catch (Exception e){
            callResult.error("AliyunOssClient.uploadFileByUrl" + e);
        }finally {
            ossClient.shutdown();
        }
    }

    public void downloadFile(CallResult<?> callResult, String key, String toFile){
        OSSClient ossClient = new OSSClient(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
        try{
            ObjectMetadata result = ossClient.getObject(new GetObjectRequest(ossConfig.getBacketName(), key), new File(toFile));
            System.out.println(result);
        } catch (Throwable t) {
            callResult.error("AliyunOssClient.downloadFile" + t);
        } finally {
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
