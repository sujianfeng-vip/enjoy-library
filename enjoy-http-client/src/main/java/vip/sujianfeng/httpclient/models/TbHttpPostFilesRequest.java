package vip.sujianfeng.httpclient.models;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.InputStreamBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * author SuJianFeng
 * createTime  2020/1/6 13:22
 * http上传文件请求
 **/
public class TbHttpPostFilesRequest extends TbHttpRequest {

    private Map<String, String> formParams;
    private Map<String, InputStreamBody> fileMap;

    public static TbHttpPostFilesRequest instance(String url, Map<String, String> headers, Map<String, String> formParams, Map<String, File> fileMap) throws FileNotFoundException {
        Map<String, InputStreamBody> _map = new HashMap<>();
        for (Map.Entry<String, File> entry : fileMap.entrySet()) {
            FileInputStream fileInputStream = new FileInputStream(entry.getValue());
            InputStreamBody inputStreamBody = new InputStreamBody(fileInputStream, ContentType.MULTIPART_FORM_DATA, entry.getValue().getName());
            _map.put(entry.getKey(), inputStreamBody);
        }
        return new TbHttpPostFilesRequest(url, headers, formParams, _map);
    }

    public TbHttpPostFilesRequest(String url, Map<String, String> headers, Map<String, String> formParams, Map<String, InputStreamBody> fileMap) {
        super(url, headers, new HashMap<>());
        this.fileMap = fileMap;
        this.formParams = formParams;
    }

    public Map<String, InputStreamBody> getFileMap() {
        return fileMap;
    }

    public void setFileMap(Map<String, InputStreamBody> fileMap) {
        this.fileMap = fileMap;
    }

    public Map<String, String> getFormParams() {
        return formParams;
    }

    public void setFormParams(Map<String, String> formParams) {
        this.formParams = formParams;
    }
}
