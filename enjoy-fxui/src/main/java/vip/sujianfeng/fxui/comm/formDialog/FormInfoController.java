package vip.sujianfeng.fxui.comm.formDialog;

import javafx.application.Platform;
import javafx.scene.control.Label;
import vip.sujianfeng.fxui.annotations.FxForm;
import vip.sujianfeng.fxui.forms.base.FxBaseController;

/**
 * @Author SuJianFeng
 * @Date 2022/12/14
 * @Description
 **/
@FxForm(value = "/fxml/form-info.fxml", title = "提示")
public class FormInfoController extends FxBaseController {
    public Label lblMsg;


    public void onOk() {
        this.closeForm();
    }

    public void error(String message) {
        Platform.runLater(()-> {
            lblMsg.setText(message);
        });
    }

    public void info(String message) {
        Platform.runLater(()-> {
            lblMsg.setText(message);
        });
    }

    @Override
    public boolean formResizable() {
        return false;
    }
}
