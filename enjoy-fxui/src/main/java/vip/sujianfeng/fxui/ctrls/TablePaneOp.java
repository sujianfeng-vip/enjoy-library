package vip.sujianfeng.fxui.ctrls;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableRow;
import javafx.scene.layout.HBox;

import java.util.List;

/**
 * author SuJianFeng
 * createTime  2019/9/14 11:54
 * 表格行操作按钮面板
 **/
public class TablePaneOp<S, T> extends HBox {

    public interface CreateButtons {
        List<Button> build(TableCellOp<?, ?> tableRow);
    }

    public TablePaneOp(TableCellOp<S, T> tableCellOp, CreateButtons createButtons) {
        this.getStyleClass().add("table-op-pane");
        this.setSpacing(5);
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(createButtons.build(tableCellOp));
    }

}
