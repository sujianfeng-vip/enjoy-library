package vip.sujianfeng.utils.dataset.intf;

/**
 * @Author SuJianFeng
 * @Date 2023/7/5
 * @Description
 **/
public interface MergerIntercept<A, B> {
    void rowMerge(A mainRow, B subRow);
}
