package vip.sujianfeng.weixin.model;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by sujianfeng on 2016/7/29.
 */
public class WxTemplate implements Serializable {
    private String template_id;
    private String touser;
    private String url;
    private String topcolor;
    private Map<String,TemplateData> data;

    private Map<String, String> miniprogram;

    public String getTemplate_id() {
        return template_id;
    }
    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }
    public String getTouser() {
        return touser;
    }
    public void setTouser(String touser) {
        this.touser = touser;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getTopcolor() {
        return topcolor;
    }
    public void setTopcolor(String topcolor) {
        this.topcolor = topcolor;
    }
    public Map<String, TemplateData> getData() {
        return data;
    }
    public void setData(Map<String, TemplateData> data) {
        this.data = data;
    }

    public Map<String, String> getMiniprogram() {
        return miniprogram;
    }

    public void setMiniprogram(Map<String, String> miniprogram) {
        this.miniprogram = miniprogram;
    }
}
