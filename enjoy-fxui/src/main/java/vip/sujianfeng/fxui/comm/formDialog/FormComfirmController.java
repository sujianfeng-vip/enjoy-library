package vip.sujianfeng.fxui.comm.formDialog;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.stage.WindowEvent;
import vip.sujianfeng.fxui.annotations.FxForm;
import vip.sujianfeng.fxui.forms.base.FxBaseController;
import vip.sujianfeng.utils.comm.StringUtilsEx;

/**
 * author SuJianFeng
 * createTime  2022/12/14
 * @Description
 **/
@FxForm(value = "/fxml/form-confirm.fxml", title = "提示")
public class FormComfirmController extends FxBaseController {
    public Label lblMsg;

    private IConfirm confirm = null;
    private boolean clickOk = false;

    public interface IConfirm {
        void run(boolean clickOk);
    }

    public void init(String title, String message, IConfirm confirm) {
        this.confirm = confirm;
        Platform.runLater(()->{
            if (StringUtilsEx.isNotEmpty(title)) {
                this.getStage().setTitle(title);
            }
            lblMsg.setText(message);
        });
    }

    public void onOk() throws Exception {
        clickOk = true;
        this.closeForm();
    }

    public void onCancel() throws Exception {
        this.closeForm();
    }

    @Override
    protected void onHidden(WindowEvent windowEvent) throws Exception {
        super.onHidden(windowEvent);
        if (confirm != null) {
            confirm.run(clickOk);
        }
    }

    @Override
    public boolean formResizable() {
        return false;
    }
}
