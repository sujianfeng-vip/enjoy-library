package vip.sujianfeng.fxui;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import vip.sujianfeng.fxui.dsmodel.FxApiResult;
import vip.sujianfeng.fxui.dsmodel.FxPageRows;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * author SuJianFeng
 * createTime  2022/11/7
 * @Description
 **/
public class FastJsonTest {

    public static void main(String[] args) {
        FxApiResult<FxPageRows<DemoBean>> it = new FxApiResult<>();
        it.setCode(2000);
        FxPageRows<DemoBean> data = new FxPageRows<>("", 1, 100);
        it.setData(data);
        data.setRows(new ArrayList<>());
        data.getRows().add(new DemoBean("张三"));
        data.getRows().add(new DemoBean("李四"));
        parser(it, DemoBean.class);
    }

    public static <T> void parser(FxApiResult<FxPageRows<T>> it, Class<T> cls) {
        String json = JSON.toJSONString(it);
        Type type = new TypeReference<FxApiResult<FxPageRows<T>>>(cls) {}.getType();
        json = "{\"attributes\":{},\"code\":5000,\"message\":\"未登录!\",\"success\":false}";
        FxApiResult<FxPageRows<T>> o = JSON.parseObject(json, type);
        System.out.println(o);

    }
}
