package vip.sujianfeng.fxui.forms.base;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.sujianfeng.fxui.utils.FxFormUtils;
import vip.sujianfeng.utils.comm.StringUtilsEx;
import vip.sujianfeng.utils.define.CallResult;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * author SuJianFeng
 * createTime  2019/9/14 13:20
 **/
public abstract class FxBaseController implements Initializable {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    private Stage stage;
    private Object container = null;
    private Object[] params;
    private Object parent;
    protected Object closeSrc = null;

    public Stage getStage() {
        return this.stage;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setOnShown(windowEvent -> {
            try {
                onShown(windowEvent);
            } catch (Exception e) {
                e.printStackTrace();
                FxFormUtils.showLogForm(e);
            }
        });
        stage.setOnShowing(windowEvent -> {
            try {
                onShowing(windowEvent);
            } catch (Exception e) {
                e.printStackTrace();
                FxFormUtils.showLogForm(e);
            }
        });
        stage.setOnCloseRequest(windowEvent -> {
            try {
                onCloseRequest(windowEvent);
            } catch (Exception e) {
                e.printStackTrace();
                FxFormUtils.showLogForm(e);
            }
        });
        stage.setOnHidden(windowEvent -> {
            try {
                onHidden(windowEvent);
            } catch (Exception e) {
                e.printStackTrace();
                FxFormUtils.showLogForm(e);
            }
        });
        stage.setOnHiding(windowEvent -> {
            try {
                onHiding(windowEvent);
            } catch (Exception e) {
                e.printStackTrace();
                FxFormUtils.showLogForm(e);
            }
        });
    }

    public Node getRootNode() {
        assert this.stage != null;
        return this.stage.getScene().getRoot();
    }

    public <T extends FxBaseController> T show() {
        this.stage.show();
        return (T) this;
    }

    public void showAndWait() {
        this.stage.showAndWait();
    }


    public void putParams(Object... params){
        this.params = params;
        loadPageData(params);
    }

    public String getTitle() {
        return this.stage.getTitle();
    }

    public void closeForm(){
        closeForm(null);
    }

    public void closeForm(Object closeSrc){
        this.closeSrc = closeSrc;
        try {
            this.stage.close();
        }finally {
            this.closeSrc = null;
        }
    }

    @Override
    final public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void loadPageData(Object... params) {

    }

    public void initPage(Object... params) {

    }

    public void updateMainStylesheet() {
        if (StringUtilsEx.isNotEmpty(FxFormUtils.MAIN_STYLESHEET_FILE)) {
            this.getStage().getScene().getStylesheets().clear();
            this.getStage().getScene().getStylesheets().add(FxFormUtils.MAIN_STYLESHEET_FILE);
        }
    }


    protected void onShowing(WindowEvent windowEvent) throws Exception{
    }


    protected void onShown(WindowEvent windowEvent) throws Exception{

    }

    protected void onCloseRequest(WindowEvent windowEvent)throws Exception {

    }


    protected void onHiding(WindowEvent windowEvent)throws Exception {

    }


    protected void onHidden(WindowEvent windowEvent)throws Exception {
    }

    protected void require(boolean check, String msg, CallResult<?> op) {
        if (!check) {
            op.error(msg);
        }
    }
    public boolean formResizable(){
        return true;
    }

    public Object getContainer() {
        return container;
    }

    public void setContainer(Object container) {
        this.container = container;
    }

    public Object[] getParams() {
        return params;
    }

    public Object getParent() {
        return parent;
    }

    public void setParent(Object parent) {
        this.parent = parent;
    }
}
