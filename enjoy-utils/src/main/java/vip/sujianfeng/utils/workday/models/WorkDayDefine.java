package vip.sujianfeng.utils.workday.models;

import java.util.List;

public class WorkDayDefine {
    private String desc;
    private List<WorkYearDefine> define;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<WorkYearDefine> getDefine() {
        return define;
    }

    public void setDefine(List<WorkYearDefine> define) {
        this.define = define;
    }
}
