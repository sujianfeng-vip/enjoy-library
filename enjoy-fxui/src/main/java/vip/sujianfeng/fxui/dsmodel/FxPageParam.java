package vip.sujianfeng.fxui.dsmodel;

/**
 * @Author SuJianFeng
 * @Date 2022/11/7
 * @Description
 **/
public class FxPageParam {
    private int pageNo = 1;
    private int pageSize = 10;
    private String keyword;
    private String orderBy;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
