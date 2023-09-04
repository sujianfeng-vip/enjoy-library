package vip.sujianfeng.fxui.comm.formLog;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import vip.sujianfeng.fxui.annotations.FxForm;
import vip.sujianfeng.fxui.forms.base.FxBaseController;
import vip.sujianfeng.utils.comm.StringUtilsEx;

/**
 * @author SuJianFeng
 * @date 2019/9/7 22:37
 **/
@FxForm(value = "/fxml/form-log.fxml", title = "错误日志")
public class FormLogController extends FxBaseController {
    public TextArea txtDetailLog = new TextArea();
    public Label lblTitle;
    public BorderPane paneBody;

    private String log;
    private Throwable throwable;

    @Override
    public void loadPageData(Object... params) {
        super.loadPageData(params);
        this.txtDetailLog.setEditable(false);
    }

    public void updateMessage(String msg, Object e) {
        if (StringUtilsEx.isEmpty(msg)) {
            msg = e.toString();
        }
        this.log = msg;
        Platform.runLater(()->{
            lblTitle.setText(this.log);
            txtDetailLog.clear();
            txtDetailLog.appendText(this.log);
            throwable = e instanceof Throwable ? (Throwable) e : null;
            if (throwable != null){
                for (StackTraceElement element : throwable.getStackTrace()) {
                    txtDetailLog.appendText(String.format("\n%s.%s[%s]", element.getClassName(), element.getMethodName(), element.getLineNumber()));
                }
            }
        });
    }


    public void btnOk(ActionEvent actionEvent) {
        closeForm();
    }

    public void showDetail(ActionEvent actionEvent) {
        if (paneBody.getCenter() != null)
            return;
        paneBody.setCenter(txtDetailLog);
    }
}
