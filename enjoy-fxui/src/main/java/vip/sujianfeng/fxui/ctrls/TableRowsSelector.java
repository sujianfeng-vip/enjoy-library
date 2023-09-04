package vip.sujianfeng.fxui.ctrls;

import java.util.HashMap;
import java.util.Map;

/**
 * @author SuJianFeng
 * @date 2019/9/14 11:15
 **/
public class TableRowsSelector {

    private Map<String, Boolean> selectedMap = new HashMap<>();

    public boolean isSelected(String id){
        return selectedMap.containsKey(id) && selectedMap.get(id);
    }

    public void setSelected(String id, boolean selected){
        selectedMap.put(id, selected);
    }

    public void clear(){
        selectedMap.clear();
    }

}
