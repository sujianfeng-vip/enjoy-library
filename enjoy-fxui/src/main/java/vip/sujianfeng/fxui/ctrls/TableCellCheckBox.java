package vip.sujianfeng.fxui.ctrls;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;

/**
 * author SuJianFeng
 * createTime  2019/9/14 10:46
 **/
public class TableCellCheckBox<S, T> extends TableCell<S, T> {

    private ObservableValue<T> ov;
    private final CheckBox checkBox;
    private CheckBoxEvent checkBoxEvents;

    public interface CheckBoxEvent {
        void onAction(TableCellCheckBox<?, ?> tableCell, Object item, boolean selected);
    }

    public TableCellCheckBox(CheckBoxEvent checkBoxEvents) {
        this.checkBox = new CheckBox();
        setGraphic(this.checkBox);
        this.setAlignment(Pos.CENTER);
        this.checkBoxEvents = checkBoxEvents;
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            //如果此列为空默认不添加元素
            setText(null);
            setGraphic(null);
        } else {
            //初始化为不选中
            this.checkBox.setSelected(false);
            setGraphic(this.checkBox);
            this.checkBox.setOnAction(event -> {
                T row = (T) this.getTableRow().getItem();
                //选择框点击进来
                checkBoxEvents.onAction(this, row, this.checkBox.isSelected());
            });
        }
    }
}
