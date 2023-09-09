package vip.sujianfeng.fxui.dsmodel;

/**
 * author SuJianFeng
 * createTime  2019/9/17 8:26
 * 资源模型
 * 前端操作界面与数据库操作之间的模型
 **/
public class DataSourceModel {
    private String title;
    private Class<?> modelClassName;
    private Class<?> listControllerClass;
    private Class<?> editControllerClass;
    private Class<?> viewControllerClass;

    public Class<?> getModelClassName() {
        return modelClassName;
    }

    public void setModelClassName(Class<?> modelClassName) {
        this.modelClassName = modelClassName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Class<?> getListControllerClass() {
        return listControllerClass;
    }

    public void setListControllerClass(Class<?> listControllerClass) {
        this.listControllerClass = listControllerClass;
    }

    public Class<?> getEditControllerClass() {
        return editControllerClass;
    }

    public void setEditControllerClass(Class<?> editControllerClass) {
        this.editControllerClass = editControllerClass;
    }

    public Class<?> getViewControllerClass() {
        return viewControllerClass;
    }

    public void setViewControllerClass(Class<?> viewControllerClass) {
        this.viewControllerClass = viewControllerClass;
    }
}
