package vip.sujianfeng.fxui.dsmodel;

/**
 * author SuJianFeng
 * createTime  2022/11/21
 * Description
 **/
public class FxComboboxData {
    private Object obj;
    private String value = "";
    private String title = "";

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
