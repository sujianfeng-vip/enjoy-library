package vip.sujianfeng.fxui.menus;

import javafx.scene.control.TreeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.sujianfeng.fxui.annotations.FxMenu;
import vip.sujianfeng.utils.comm.ConfigUtils;
import vip.sujianfeng.utils.comm.ConvertUtils;
import vip.sujianfeng.utils.comm.StringUtilsEx;
import vip.sujianfeng.utils.scan.ScanFilter;
import vip.sujianfeng.utils.scan.ScannerUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author 苏建锋
 * @create 2019-09-10 4:56
 */
public class MenuTreeBuilder {

    private static final String MENU_TREE_YML = "menus/menu-tree.yml";

    private static Logger logger = LoggerFactory.getLogger(MenuTreeBuilder.class);

    private volatile static TreeItem<MenuTreeNode> rootNode = null;

    private static void initRoot(){
        if (rootNode != null) return;
        //这个是缓存用途，界面上不会用到这个根节点
        rootNode = new TreeItem<>(new MenuTreeNode(null, "root", "根节点"));
        Map menuTree = ConfigUtils.getYml(MENU_TREE_YML);
        buildTreeNodes(rootNode, (List<Map>) menuTree.get("children"));
    }
    /**
     * 初始化：加载菜单树，并扫描所有注解了@FxMenu的功能类
     */
    public static void init(){
        initRoot();
        String basePackage = "cc.twobears";
        String endStr = ".class";
        ScanFilter scanFilter = name -> {
            if (name == null) return false;
            if (name.contains(".git")) return false;
            if (name.contains(".gradle")) return false;
            if (name.contains(".idea")) return false;
            name = name.replace("\\", ".");
            name = name.replace("/", ".");
            if (!name.toLowerCase().contains(basePackage)) return false;
            if (!name.toLowerCase().contains(endStr)) return false;
            return true;
        };
        //List<String> list = ScannerUtils.search(scanFilter, FxFormUtils.class);
        List<String> list = ScannerUtils.search(scanFilter, ConfigUtils.getProperty("user.dir"));
        for (String str : list) {
            str = str.replace("\\", ".");
            str = str.replace("/", ".");
            str = basePackage + StringUtilsEx.rightStr(str, basePackage);
            str = StringUtilsEx.leftStrEx(str, endStr);
            try {
                Class<?> aClass = Class.forName(str);
                addMenuByClass(aClass);
            } catch (ClassNotFoundException e) {
                logger.info("无法加载类: " + str);
            }
        }
    }

    public static void init(Class<?>... classes){
        initRoot();
        for (Class<?> aClass : classes) {
            addMenuByClass(aClass);
        }
    }

    private static void addMenuByClass(Class<?> aClass){
        FxMenu fxMenu = aClass.getAnnotation(FxMenu.class);
        if (fxMenu != null){
            TreeItem<MenuTreeNode> treeNode = getTreeItem(rootNode, aClass.getName());
            //扫描后，会出现重复，这里进行判断，避免重复创建
            if (treeNode == null){
                TreeItem<MenuTreeNode> parentNode = getTreeItem(rootNode, fxMenu.parentMenuId());
                if (parentNode != null){
                    treeNode = new TreeItem<>(new MenuTreeNode(aClass, aClass.getName(), fxMenu.title()));
                    parentNode.getChildren().add(treeNode);
                }
            }
        }
    }

    private static TreeItem<MenuTreeNode> getTreeItem(TreeItem<MenuTreeNode> treeNode, String id){
        if (StringUtilsEx.sameText(id, treeNode.getValue().getId())){
            return treeNode;
        }
        for (TreeItem<MenuTreeNode> childNode : treeNode.getChildren()) {
            TreeItem<MenuTreeNode> result = getTreeItem(childNode, id);
            if (result != null){
                return result;
            }
        }
        return null;
    }


    private static void buildTreeNodes(TreeItem<MenuTreeNode> parentNode, List<Map> menus){
        for (Map menu : menus) {
            String id = ConvertUtils.cStr(menu.get("id"));
            String name = ConvertUtils.cStr(menu.get("name"));
            TreeItem<MenuTreeNode> treeNode = new TreeItem<>();
            treeNode.setValue(new MenuTreeNode(null, id, name));
            parentNode.getChildren().add(treeNode);
            Object children = menu.get("children");
            if (children != null && children instanceof List){
                buildTreeNodes(treeNode, (List<Map>) children);
            }
        }
    }

    /**
     * 创建菜单树
     * @param rootNode
     * @param userId
     */
    public static void buildAppMenuTree(TreeItem<MenuTreeNode> rootNode, String userId){
        rootNode.getChildren().addAll(MenuTreeBuilder.rootNode.getChildren());
    }

}
