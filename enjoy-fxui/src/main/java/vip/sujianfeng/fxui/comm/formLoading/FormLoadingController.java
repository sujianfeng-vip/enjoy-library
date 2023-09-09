package vip.sujianfeng.fxui.comm.formLoading;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import vip.sujianfeng.fxui.annotations.FxForm;
import vip.sujianfeng.fxui.forms.base.FxBaseController;

/**
 * author SuJianFeng
 * createTime  2019/9/8 11:55
 **/
@FxForm(value = "/fxml/form-loading.fxml", title = "loading...")
public class FormLoadingController extends FxBaseLoadingForm {
    public Label lblLoadingMsg;

    @Override
    public boolean formResizable() {
        return false;
    }

    @Override
    public void updateMsg(String msg) {
        lblLoadingMsg.setText(msg);
    }
}
