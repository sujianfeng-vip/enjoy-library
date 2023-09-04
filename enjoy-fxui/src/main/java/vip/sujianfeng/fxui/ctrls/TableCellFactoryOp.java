package vip.sujianfeng.fxui.ctrls;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * @author SuJianFeng
 * @date 2019/9/23 7:38
 * Table的操作列
 **/
public class TableCellFactoryOp<T, F> implements Callback<TableColumn<T, F>, TableCell<T, F>> {

    private TablePaneOp.CreateButtons createButtons;

    public TableCellFactoryOp(TablePaneOp.CreateButtons createButtons){
        this.createButtons = createButtons;
    }

    @Override
    public TableCell<T, F> call(TableColumn<T, F> tableColumn) {
        return new TableCellOp<>(this.createButtons);
    }


}
