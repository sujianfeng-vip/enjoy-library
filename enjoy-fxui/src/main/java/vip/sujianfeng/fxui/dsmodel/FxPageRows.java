package vip.sujianfeng.fxui.dsmodel;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * author SuJianFeng
 * createTime  2019/1/31 9:08
 * 分页数据
 **/
public class FxPageRows<T> {

    private List<T> rows;
    private int pageIndex;
    private int pageSize;
    private int totalSize;
    private String condition;

    public <X> FxPageRows<X> clone(Class<X> t) {
        FxPageRows<X> result = new FxPageRows<>(this.condition, this.pageIndex, this.pageSize);
        if (this.getRows() != null) {
            result.setRows(JSON.parseArray(JSON.toJSONString(this.getRows()), t));
        }
        return result;
    }

    public FxPageRows(String condition, int pageIndex, int pageSize){
        this.condition = condition;
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
