package vip.sujianfeng.fxui;

import vip.sujianfeng.fxui.dsmodel.FxApiResult;
import vip.sujianfeng.fxui.dsmodel.FxPageParam;
import vip.sujianfeng.fxui.dsmodel.FxPageRows;
import vip.sujianfeng.fxui.utils.FxApiUtils;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * author SuJianFeng
 * createTime  2022/11/7
 * @Description
 **/
public class FxApiUtilsTest {

    public static void main(String[] args) {
        AtomicReference<FxApiResult<FxPageRows<DemoBean>>> it = new AtomicReference<>(new FxApiResult<>());
        FxApiUtils.queryPage("https://www.sujianfeng.vip/game-assistant-api/game-script-define/queryPage", new HashMap<>(), new FxPageParam(), DemoBean.class, it);
        System.out.println(it.get());
    }
}
