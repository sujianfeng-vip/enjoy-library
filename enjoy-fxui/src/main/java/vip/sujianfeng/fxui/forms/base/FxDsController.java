package vip.sujianfeng.fxui.forms.base;

import com.alibaba.fastjson2.JSON;
import vip.sujianfeng.fxui.dsmodel.FxBaseModel;
import vip.sujianfeng.fxui.dsmodel.FxPageParam;
import vip.sujianfeng.fxui.interfaces.FxDiskDataHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * author SuJianFeng
 * createTime  2019/9/17 17:32
 * 配置资源模型注解的解析基类
 **/
public abstract class FxDsController<T extends FxBaseModel, P extends FxPageParam, D extends FxDiskDataHandler<T>> extends FxBaseController {

    private Class<T> modelClass;
    private Class<P> pageParamClass;
    private Class<D> dataHandlerClass;
    private D dataHandler;
    private P pageParam;

    public FxDsController() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        modelClass = (Class<T>) params[0];
        pageParamClass = (Class<P>) params[1];
        dataHandlerClass = (Class<D>) params[2];
        //利用JSON进行反序列化来初始化泛型类
        pageParam = JSON.parseObject("{}", pageParamClass);
        dataHandler = JSON.parseObject("{}", dataHandlerClass);
    }

    public Class<T> getModelClass() {
        return modelClass;
    }

    public Class<P> getPageParamClass() {
        return pageParamClass;
    }

    public Class<D> getDataHandlerClass() {
        return dataHandlerClass;
    }

    public P getPageParam() {
        return pageParam;
    }

    public D getDataHandler() {
        return dataHandler;
    }
}
