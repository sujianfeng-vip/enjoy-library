package vip.sujianfeng.fxui.interfaces;

import vip.sujianfeng.fxui.dsmodel.FxBaseModel;
import vip.sujianfeng.fxui.dsmodel.FxManyIdParam;
import vip.sujianfeng.fxui.dsmodel.FxPageParam;
import vip.sujianfeng.fxui.dsmodel.FxPageRows;
import vip.sujianfeng.utils.define.CallResult;

/**
 * 数据处理接口
 * @Author SuJianFeng
 * @Date 2022/11/4
 * @Description
 **/
public interface FxDiskDataHandler<T extends FxBaseModel> {
    CallResult<Integer> delete(FxManyIdParam param);
    CallResult<FxPageRows<T>> queryPage(FxPageParam param);
    CallResult<T> queryOne(String id);
    CallResult<T> add(T model);
    CallResult<T> update(T model);
}
