package vip.sujianfeng.fxui.utils;


import vip.sujianfeng.fxui.comm.formLoading.LoadingUtils;
import vip.sujianfeng.fxui.dsmodel.FxPageRows;
import vip.sujianfeng.httpclient.TbHttpPost;
import vip.sujianfeng.utils.define.CallResult;
import vip.sujianfeng.utils.intf.CommEvent;

import java.util.List;
import java.util.Map;

/**
 * @Author SuJianFeng
 * @Date 2022/12/30
 * @Description
 **/
public class FxApiAsyncUtils {
    private static TbHttpPost tbHttpPost = new TbHttpPost();

    static {
        tbHttpPost.getTbHttpClientConfig().setConnectTimeout(20000);
        tbHttpPost.getTbHttpClientConfig().setSocketTimeout(20000);
        tbHttpPost.getTbHttpClientConfig().setConnectionRequestTimeout(20000);
    }

    public static <P, T> void post(String url, Map<String, String> headers, P param, Class<T> t, CommEvent<CallResult<T>> callBack) {
        postLoading(url, headers, param, t, callBack);
    }

    public static <P, T> void postList(String url, Map<String, String> headers, P param, Class<T> t, CommEvent<CallResult<List<T>>> callBack) {
        postLoading(url, headers, param, String.class, (CallResult<String> baseOp)->{
            CallResult<List<T>> result = new CallResult<>();
            result.getAttributes().putAll(baseOp.getAttributes());
            result.setCode(baseOp.getCode());
            result.setMessage(baseOp.getMessage());
            if (baseOp.getData() != null) {
                result = baseOp.cloneList(t);
            }
            callBack.call(result);

        });
    }

    public static <P, T> void postPage(String url, Map<String, String> headers, P param, Class<T> t, CommEvent<CallResult<FxPageRows<T>>> callBack) {
        postLoading(url, headers, param, String.class, (CallResult<String> tmp1)->{
            callBack.call(FxApiUtils.parsePage(tmp1, t));
        });
    }

    private static <T> void postLoading(String url, Map<String, String> headers, Object param, Class<T> t, CommEvent<CallResult<T>> callBack) {
        LoadingUtils.loadBySilence(()->{
            CallResult<?> it = FxApiUtils.postBase(url, headers, param);
            callBack.call(it.clone(t));
        });
    }

}
