package vip.sujianfeng.enums;

/**
 * author SuJianFeng
 * createTime  2022/10/12
 * Description
 **/
public enum DataStatus {
    Normal(0, "Normal"),
    Delete(1, "Delete"),
    LogOff(2, "LogOff");

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
