package vip.sujianfeng.fxui;

import vip.sujianfeng.fxui.dsmodel.FxBaseModel;

/**
 * author SuJianFeng
 * createTime  2022/11/7
 * @Description
 **/
public class DemoBean extends FxBaseModel {
    public DemoBean(String name) {
        this.name = name;
    }

    private String name;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
