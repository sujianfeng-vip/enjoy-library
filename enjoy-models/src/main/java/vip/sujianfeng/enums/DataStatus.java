package vip.sujianfeng.enums;

/**
 * 数据状态
 * @Author SuJianFeng
 * @Date 2022/10/12
 * @Description
 **/
public enum DataStatus {
    Normal(0, "正常"),
    Delete(1, "逻辑删除"),
    LogOff(2, "注销");

    DataStatus(int value, String label) {
        this.value = value;
        this.label = label;
    }

    private int value;
    private String label;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
