package vip.sujianfeng.weixin.model;

import java.io.Serializable;

/**
 * Created by sujianfeng on 2016/7/29.
 */
public class TemplateData implements Serializable {
    private String value;
    private String color;
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
}
