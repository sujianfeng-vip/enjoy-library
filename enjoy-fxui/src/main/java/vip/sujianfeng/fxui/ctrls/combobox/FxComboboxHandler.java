package vip.sujianfeng.fxui.ctrls.combobox;

import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import vip.sujianfeng.fxui.dsmodel.FxComboboxData;

import java.util.List;
import java.util.Optional;

/**
 * author SuJianFeng
 * createTime  2023/1/16
 * Description
 **/
public class FxComboboxHandler {

    private boolean loading = false;
    private ComboBox<FxComboboxData> combobox;

    public FxComboboxHandler(ComboBox<FxComboboxData> combobox) {
        this.combobox = combobox;
        this.combobox.setConverter(new StringConverter<FxComboboxData>() {
            @Override
            public String toString(FxComboboxData item) {
                return item != null ? item.getTitle() : "";
            }
            @Override
            public FxComboboxData fromString(String string) {
                FxComboboxData item = new FxComboboxData();
                item.setValue(string);
                item.setTitle(string);
                return item;
            }
        });
    }

    public void updateList(List<FxComboboxData> list) {
        this.combobox.getItems().clear();
        this.combobox.getItems().addAll(list);
    }

    public void setValue(String value) {
        FxComboboxHandler.setValue(this.combobox, value);
    }

    public FxComboboxData getValue() {
        return this.combobox.getValue();
    }

    public ComboBox<FxComboboxData> getCombobox() {
        return combobox;
    }

    public static void setValue(ComboBox<FxComboboxData> combobox, String value) {
        Optional<FxComboboxData> find = combobox.getItems().stream().filter(it -> it.getValue().equals(value)).findAny();
        combobox.setValue(find.orElse(new FxComboboxData()));
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }
}
