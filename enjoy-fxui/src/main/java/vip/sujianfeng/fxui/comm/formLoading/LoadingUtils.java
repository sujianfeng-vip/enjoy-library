package vip.sujianfeng.fxui.comm.formLoading;

import javafx.application.Platform;
import vip.sujianfeng.fxui.interfaces.LoadRun;
import vip.sujianfeng.fxui.utils.FxFormUtils;
import vip.sujianfeng.utils.comm.StringUtilsEx;

/**
 * author sujianfeng
 * create 2019-09-19 5:24
 */
public class LoadingUtils {

    public static FxLoadingService load(LoadRun task, LoadRun afterOkRun){
        return load(task, afterOkRun, null, "");
    }


    public static FxLoadingService load(LoadRun task, LoadRun afterOkRun, LoadRun afterFailRun, String msg){
        return load(null, task, afterOkRun, afterFailRun, msg, true);
    }


    public static FxLoadingService load(FxBaseLoadingForm loadingForm, LoadRun task, LoadRun afterOkRun){
        return load(loadingForm, task, afterOkRun, null, "", true);
    }

    public static FxLoadingService load(FxBaseLoadingForm loadingForm, LoadRun task, LoadRun afterOkRun, LoadRun afterFailRun, final String msg, boolean autoClose){
        FxBaseLoadingForm frmLoading = loadingForm == null ? FxFormUtils.showLoadingForm() : loadingForm;
        assert frmLoading != null;
        FxLoadingService service = new FxLoadingService(frmLoading, task, afterOkRun, afterFailRun, autoClose);
        Platform.runLater(()->{
            String message = msg;
            if (StringUtilsEx.isEmpty(message)){
                message = "loading...";
            }
            frmLoading.updateMsg(message);
        });
        service.start();
        return service;
    }

    public static FxBaseService loadBySilence(LoadRun task, LoadRun afterOkRun, LoadRun afterFailRun) {
        FxBaseService result = new FxBaseService(task, afterOkRun, afterFailRun);
        result.start();
        return result;
    }

    public static FxBaseService loadBySilence(LoadRun task) {
        return loadBySilence(task, null, null);
    }
}
