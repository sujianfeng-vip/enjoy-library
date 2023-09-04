package vip.sujianfeng.fxui.ctrls;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * @author SuJianFeng
 * @date 2019/9/23 8:39
 **/
public class TableCellFactoryCheckBox<T, F> implements Callback<TableColumn<T, F>, TableCell<T, F>> {

    private TableCellCheckBox.CheckBoxEvent checkBoxEvent;

    public TableCellFactoryCheckBox(TableCellCheckBox.CheckBoxEvent checkBoxEvent) {
        this.checkBoxEvent = checkBoxEvent;
    }

    @Override
    public TableCell<T, F> call(TableColumn tableColumn) {
        return new TableCellCheckBox<>(checkBoxEvent);
    }
}
