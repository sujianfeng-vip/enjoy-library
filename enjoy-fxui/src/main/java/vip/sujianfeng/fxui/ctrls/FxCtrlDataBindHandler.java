package vip.sujianfeng.fxui.ctrls;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;
import vip.sujianfeng.fxui.ctrls.combobox.FxComboboxHandler;
import vip.sujianfeng.fxui.dsmodel.FxBaseModel;
import vip.sujianfeng.fxui.dsmodel.FxComboboxData;
import vip.sujianfeng.fxui.utils.FxUiCtrlUtils;
import vip.sujianfeng.utils.comm.ConvertUtils;
import vip.sujianfeng.utils.comm.StringUtilsEx;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * author SuJianFeng
 * createTime  2019/9/24 8:11
 * 控件绑定数据操作
 **/
public class FxCtrlDataBindHandler<T extends FxBaseModel> {

    private Stage stage;
    private Map<String, Node> ctrlMap = new HashMap<>();
    private T baseModel = null;

    /**
     * 锁定状态，关闭控件监听
     */
    private boolean locked = false;

    public FxCtrlDataBindHandler(Stage stage){
        this.stage = stage;
        FxUiCtrlUtils.scanNode(this.stage.getScene().getRoot(), (node) -> {
            ctrlMap.put(node.getId(), node);
        });
        addListener();
    }

    private void ctrlsUpdateValue() {
        if (this.baseModel == null) {
            return;
        }
        this.locked = true;
        try {
            for (Field field : this.baseModel.allDeclaredFields()) {
                Node node = ctrlMap.get(field.getName());
                if (node instanceof TextInputControl) {
                    TextInputControl ctrl = (TextInputControl) node;
                    Object value = this.baseModel.getFieldValue(field);
                    ctrl.setText(ConvertUtils.cStr(value));
                }
                if (node instanceof ComboBox) {
                    ComboBox<FxComboboxData> ctrl = (ComboBox<FxComboboxData>) node;
                    String value = ConvertUtils.cStr(this.baseModel.getFieldValue(field));
                    FxComboboxHandler.setValue(ctrl, value);
                }
            }
        } finally {
            this.locked = false;
        }
    }

    public void updateModelObj(T baseModel) {
        this.baseModel = baseModel;
        this.ctrlsUpdateValue();
    }


    /**
     * 绑定监听
     */
    private void addListener() {
        for (Node node : ctrlMap.values()) {
            if (node instanceof TextInputControl){
                TextInputControl ctrl = (TextInputControl) node;
                //监听数据变更
                ctrl.textProperty().addListener((o, oldValue, newValue)-> {
                    if (locked) {
                        return;
                    }
                    if (this.baseModel != null) {
                        Field field = this.baseModel.getDeclaredField(ctrl.getId());
                        if (field != null) {
                            Object v = this.baseModel.getFieldValue(field);
                            if (!StringUtilsEx.sameText(v, newValue)) {
                                this.baseModel.setFieldValue(field, newValue);
                            }
                        }
                    }
                });
            }
            if (node instanceof ComboBox) {
                ComboBox<FxComboboxData> ctrl = (ComboBox<FxComboboxData>) node;
                ctrl.valueProperty().addListener((o, oldValue, newValue)-> {
                    if (locked) {
                        return;
                    }
                    if (this.baseModel != null) {
                        Field field = this.baseModel.getDeclaredField(ctrl.getId());
                        if (field != null) {
                            Object v = this.baseModel.getFieldValue(field);
                            if (!StringUtilsEx.sameText(v, newValue.getValue())) {
                                this.baseModel.setFieldValue(field, newValue.getValue());
                            }
                        }
                    }
                });

            }
        }
    }


    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
