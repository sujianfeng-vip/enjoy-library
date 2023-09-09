package vip.sujianfeng.fxui.comm.formLoading;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.sujianfeng.fxui.interfaces.LoadRun;

/**
 * author sujianfeng
 * create 2019-09-19 5:21
 */
public class FxBaseService extends Service<Integer> {

    private LoadRun task;

    private LoadRun afterSuccess;

    private LoadRun afterFail;

    private static long MIN_DELAYTIME = 500;

    private long beginTime;


    public FxBaseService(LoadRun task, LoadRun afterSuccess, LoadRun afterFail) {
        this.task = task;
        this.afterSuccess = afterSuccess;
        this.afterFail = afterFail;
        this.beginTime = System.currentTimeMillis();
    }

    @Override
    protected Task<Integer> createTask() {
        return new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                try {
                    Platform.runLater(()-> beforeRun());
                    task.run();
                } catch (Exception ex) {
                    logger.error(ex.toString(), ex);
                    Platform.runLater(() -> {
                        try {
                            onException(ex);
                            if (afterFail != null) {
                                afterFail.run();
                            }
                        } catch (Exception e) {
                            logger.error(e.toString(), e);
                            onException(e);
                        }
                    });
                    return null;
                }
                long remainTime = MIN_DELAYTIME - (System.currentTimeMillis() - beginTime);
                remainTime = remainTime > 200 ? remainTime : 200;
                Thread.sleep(remainTime);
                Platform.runLater(() -> {
                    try {
                        if (afterSuccess != null) {
                            afterSuccess.run();
                        }
                        afterRun();
                    } catch (Exception e) {
                        logger.error(e.toString(), e);
                        onException(e);
                    }
                });
                return null;
            }
        };
    }

    protected void beforeRun() {

    }

    protected void afterRun() {

    }

    protected void onException(Exception e) {

    }

    private static Logger logger = LoggerFactory.getLogger(FxBaseService.class);

}

