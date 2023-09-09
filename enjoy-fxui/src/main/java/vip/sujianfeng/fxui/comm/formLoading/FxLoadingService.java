package vip.sujianfeng.fxui.comm.formLoading;

import vip.sujianfeng.fxui.interfaces.LoadRun;
import vip.sujianfeng.fxui.utils.FxFormUtils;

/**
 * author sujianfeng
 * @create 2019-09-19 5:21
 */
public class FxLoadingService extends FxBaseService {

    private FxBaseLoadingForm frmLoading;
    /**
     * 任务完成后，是否自动关闭提示窗口
     */
    private boolean autoClose;

    public FxLoadingService(FxBaseLoadingForm frmLoading, LoadRun task, LoadRun afterSuccess, LoadRun afterFail, boolean autoClose){
        super(task, afterSuccess, afterFail);
        this.frmLoading = frmLoading;
        this.autoClose = autoClose;
    }

    @Override
    protected void beforeRun() {
        super.beforeRun();
        frmLoading.getStage().show();
    }

    @Override
    protected void afterRun() {
        super.afterRun();
        frmLoading.updateMsg("执行完成!");
        //关闭加载提示窗体
        if (autoClose){
            frmLoading.getStage().close();
        }
    }

    @Override
    protected void onException(Exception e) {
        super.onException(e);
        frmLoading.getStage().close();
        FxFormUtils.showLogForm(e.toString(), e);
    }
}
