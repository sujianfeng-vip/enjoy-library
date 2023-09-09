package vip.sujianfeng.fxui.utils;

import javafx.scene.Node;
import javafx.scene.Parent;
import vip.sujianfeng.utils.comm.StringUtilsEx;

import java.util.Set;

/**
 * author SuJianFeng
 * createTime  2022/10/28
 * Description
 **/
public class FxUiCtrlUtils {

    public static Node lookupNode(Node node, String id, String... selectors){
        if (id.equalsIgnoreCase(node.getId())){
            return node;
        }
        for (String selector : selectors) {
            Set<Node> nodes = node.lookupAll(selector);
            for (Node tmp : nodes) {
                if (StringUtilsEx.sameText(tmp.getId(), id)){
                    return tmp;
                }
            }
        }
        return null;
    }

    public static void scanNode(Node node, IScan scan) {
        scan.run(node);
        if (node instanceof Parent) {
            for (Node subNode : ((Parent) node).getChildrenUnmodifiable()) {
                scanNode(subNode, scan);
            }
        }
    }

    public static interface IScan {
        void run(Node node);
    }

}
