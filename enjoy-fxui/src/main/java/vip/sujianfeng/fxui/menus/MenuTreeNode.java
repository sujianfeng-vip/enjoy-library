package vip.sujianfeng.fxui.menus;

/**
 * @author SuJianFeng
 * @date 2019/9/6 16:21
 * 菜单树节点
 **/
public class MenuTreeNode {

    public MenuTreeNode(Class<?> ownerClass, String id, String name){
        this.ownerClass = ownerClass;
        this.id = id;
        this.name = name;
    }

    private Class<?> ownerClass;
    private String id;
    private String parentId;
    private String name;
    private String iconUrl;

    @Override
    public String toString() {
        return this.name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Class<?> getOwnerClass() {
        return ownerClass;
    }

    public void setOwnerClass(Class<?> ownerClass) {
        this.ownerClass = ownerClass;
    }
}
