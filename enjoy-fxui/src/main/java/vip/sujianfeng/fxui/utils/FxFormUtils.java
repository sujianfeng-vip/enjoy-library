package vip.sujianfeng.fxui.utils;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import vip.sujianfeng.fxui.annotations.FxForm;
import vip.sujianfeng.fxui.comm.formLoading.FormLoadingController;
import vip.sujianfeng.fxui.comm.formLog.FormLogController;
import vip.sujianfeng.fxui.forms.base.FxBaseController;
import vip.sujianfeng.utils.comm.StringUtilsEx;
import vip.sujianfeng.utils.intf.CommEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author SuJianFeng
 * @Date 2022/12/16
 * @Description
 **/
public class FxFormUtils {

    /**
     * 全局样式文件
     */
    public static String MAIN_STYLESHEET_FILE = "";
    private static Map<String, FxBaseController> MAP = new HashMap<>();


    public static <T extends FxBaseController> T buildController(Class<T> t, Object... params)  {
        return buildController(t, Modality.NONE, null, params);
    }

    public static <T extends FxBaseController> T buildController(Class<T> t, Modality modality, Object parent, Object... params)  {
        try {
            FxForm fxForm = FXmlUtils.getFXML(t);
            FXMLLoader loader = FXmlUtils.getLoader(fxForm);
            Pane pane = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            scene.getStylesheets().add(MAIN_STYLESHEET_FILE);
            stage.initModality(modality);
            if (StringUtilsEx.isNotEmpty(fxForm.title())) {
                stage.setTitle(fxForm.title());
            }else {
                stage.setTitle(t.getName());
            }
            T controller = loader.getController();
            controller.setStage(stage);
            controller.setParent(parent);
            controller.initPage(params);
            controller.putParams(params);
            stage.setResizable(controller.formResizable());
            return controller;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends FxBaseController> T getController(Class<T> t, Modality modality, Object parent, Object... params)  {
        return getController(t.getName(), t, modality, parent, params);
    }

    public static <T extends FxBaseController> T getController(Class<T> t, Object... params)  {
        return getController(t.getName(), null, null, params);
    }

    public static <T extends FxBaseController> T getController(String key, Object... params)  {
        return getController(key, null, null, params);
    }

    public static void updateMainStyleSheet(String mainCss) {
        MAIN_STYLESHEET_FILE = mainCss;
        Platform.runLater(()-> {
            for (FxBaseController value : MAP.values()) {
                value.getStage().getScene().getStylesheets().clear();
                value.getStage().getScene().getStylesheets().add(mainCss);
            }
        });
    }

    public static <T extends FxBaseController> T getController(String key, Class<T> t, Modality modality, Object parent, Object... params)  {
        FxBaseController tmp = MAP.get(key);
        if (tmp != null) {
            tmp.putParams(params);
            tmp.getStage().toFront();
            return (T) tmp;
        }
        if (t == null) {
            return null;
        }
        T t1 = buildController(t, modality, parent, params);
        MAP.put(key, t1);
        return t1;
    }


    public static <T extends FxBaseController> void showAndWaitForm(Class<T> controllerCls, CommEvent<T> beforeShow, Object... params) {
        T controller = buildController(controllerCls, Modality.APPLICATION_MODAL, null, params);
        controller.getStage().initModality(Modality.APPLICATION_MODAL);
        if (beforeShow != null) {
            beforeShow.call(controller);
        }
        controller.showAndWait();
    }

    public static <T extends FxBaseController> void showAndWaitFormEx(Class<T> controllerCls, CommEvent<T> beforeShow) {
        showAndWaitForm(controllerCls, beforeShow);
    }

    public static <T extends FxBaseController> void showAndWaitForm(Class<T> controllerCls, Object... params) {
        showAndWaitForm(controllerCls, null, params);
    }

    public static <T extends FxBaseController> T showNormalForm(Class<T> controllerCls, Object... params) {
        return getController(controllerCls, Modality.NONE, null, params).show();
    }

    public static <T extends FxBaseController> T showMaxForm(Class<T> controllerCls, Object... params) {
        T controller = getController(controllerCls, Modality.NONE, null, params);
        controller.getStage().setMaximized(true);
        return controller.show();
    }

    public static <T extends FxBaseController> T showModalForm(Class<T> controllerCls, Object... params) {
        return getController(controllerCls, Modality.APPLICATION_MODAL, null, params).show();
    }

    public static FormLoadingController showLoadingForm() {
        FormLoadingController controller = buildController(FormLoadingController.class, Modality.NONE, null);
        controller.getStage().setTitle("加载中...");
        controller.getStage().setAlwaysOnTop(true);
        return controller.show();
    }

    public static void showLogForm(String msg) {
        showLogForm(msg, null);
    }
    public static void showLogForm(Throwable e) {
        showLogForm(null, e);
    }
    public static void showLogForm(String msg, Throwable e) {
        FormLogController controller = buildController(FormLogController.class, Modality.NONE, null);
        controller.updateMessage(msg, e);
        controller.getStage().setTitle("加载中...");
        controller.getStage().setAlwaysOnTop(true);
        controller.show();
    }


}
