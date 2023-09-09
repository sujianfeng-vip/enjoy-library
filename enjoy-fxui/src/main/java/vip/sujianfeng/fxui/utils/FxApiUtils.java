package vip.sujianfeng.fxui.utils;


import com.alibaba.fastjson2.JSON;
import javafx.application.Platform;
import vip.sujianfeng.fxui.comm.formLoading.FormLoadingController;
import vip.sujianfeng.fxui.comm.formLoading.LoadingUtils;
import vip.sujianfeng.fxui.dsmodel.FxPageRows;
import vip.sujianfeng.httpclient.TbHttpPost;
import vip.sujianfeng.httpclient.models.TbHttpResponse;
import vip.sujianfeng.utils.comm.StringUtilsEx;
import vip.sujianfeng.utils.define.CallResult;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * author SuJianFeng
 * createTime  2022/11/7
 * @Description
 **/
public class FxApiUtils {
    private static TbHttpPost tbHttpPost = new TbHttpPost();

    static {
        tbHttpPost.getTbHttpClientConfig().setConnectTimeout(20000);
        tbHttpPost.getTbHttpClientConfig().setSocketTimeout(20000);
        tbHttpPost.getTbHttpClientConfig().setConnectionRequestTimeout(20000);
    }

    public static <P, T> CallResult<T> post(String url, Map<String, String> headers, P param, Class<T> t) {
        return postLoading(url, headers, param, t);
    }

    public static <P, T> CallResult<List<T>> postList(String url, Map<String, String> headers, P param, Class<T> t) {
        return postLoading(url, headers, param, Object.class).cloneList(t);
    }

    public static <P, T> CallResult<FxPageRows<T>> postPage(String url, Map<String, String> headers, P param, Class<T> t) {
        CallResult<String> tmp1 = postLoading(url, headers, param, String.class);
        return parsePage(tmp1, t);
    }

    private static <T> CallResult<T> postLoading(String url, Map<String, String> headers, Object param, Class<T> t) {
        AtomicReference<CallResult<?>> ref = new AtomicReference<>();
        FxFormUtils.showAndWaitForm(FormLoadingController.class, (controller)->{
            Platform.runLater(()-> controller.lblLoadingMsg.setText("加载中...."));
            LoadingUtils.loadBySilence(()-> ref.set(postBase(url, headers, param)), controller::closeForm, controller::closeForm);
        });
        return ref.get().clone(t);
    }

    public static <T> CallResult<FxPageRows<T>> parsePage(CallResult<String> baseOp, Class<T> t) {
        CallResult<FxPageRows<T>> result = new CallResult<>();
        result.getAttributes().putAll(baseOp.getAttributes());
        result.setCode(baseOp.getCode());
        result.setMessage(baseOp.getMessage());
        if (StringUtilsEx.isNotEmpty(baseOp.getData())) {
            FxPageRows<?> op = JSON.parseObject(baseOp.getData(), FxPageRows.class);
            FxPageRows<T> data = op.clone(t);
            result.setData(data);
        }
        return result;
    }

    public static CallResult<?> postBase(String url, Map<String, String> headers, Object param) {
        CallResult<?> it;
        TbHttpResponse<String> response = tbHttpPost.post(url, headers, param);
        if (response.getCode() != 200) {
            it = new CallResult<>();
            String error = response.getError();
            if (StringUtilsEx.isEmpty(error)) {
                error = response.getContent();
            }
            it.error(error);
        }else {
            String content = StringUtilsEx.isEmpty(response.getContent()) ? "{}" : response.getContent();
            it = JSON.parseObject(content, CallResult.class);
        }
        return it;
    }

}
