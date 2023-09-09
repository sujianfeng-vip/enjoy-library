package vip.sujianfeng.fxui.ctrls;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;

import java.util.List;

/**
 * author SuJianFeng
 * createTime  2019/9/14 10:46
 **/
public class TableCellOp<S, T> extends TableCell<S, T> {

    private ObservableValue<T> ov;
    private final TablePaneOp tableOpPane;

    public TableCellOp(TablePaneOp.CreateButtons createButtons) {
        this.tableOpPane = new TablePaneOp(this, createButtons);
        this.setGraphic(this.tableOpPane);
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            //如果此列为空默认不添加元素
            setText(null);
            setGraphic(null);
        } else {
            //初始化按钮
            setGraphic(this.tableOpPane);
        }
    }
}
