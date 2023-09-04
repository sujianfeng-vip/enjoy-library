package vip.sujianfeng.utils.model;

/**
 * @Author SuJianFeng
 * @Date 2019/6/28 9:42
 * 性能计算项目
 **/
public class PerformanceItem {

    public PerformanceItem(String itemName){
        this.itemName = itemName;
        this.consumeTimes = 0;
        this.productQty = 0;
        this.printProductQty = 0;
    }

    private long beginTime;

    /**
     * 计时开始
     */
    public void begin(){
        beginTime = System.nanoTime();
    }

    /**
     * 计时结束
     * @param addProductQty 产量
     */
    public void end(long addProductQty){
        if (addProductQty > 0){
            add(System.nanoTime() - beginTime, addProductQty);
        }
    }

    /**
     * 重置清零
     */
    public void reset(){
        this.consumeTimes = 0;
        this.productQty = 0;
        this.printProductQty = 0;
    }

    private void add(long addConsumeTimes, long addProductQty){
        this.consumeTimes += addConsumeTimes;
        this.productQty += addProductQty;
        this.printProductQty += addProductQty;
    }

    /**
     * 标题
     */
    private String itemName;

    /**
     * 耗费时间（纳秒）
     */
    private long consumeTimes;

    /**
     * 产出数量
     */
    private long productQty;
    private long printProductQty;

    /**
     * 平均每秒产出量
     */
    public long getProductQtyBySecond(){
        return productQty * 1000 * 1000 * 1000 / consumeTimes;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public long getConsumeTimes() {
        return consumeTimes;
    }

    public void setConsumeTimes(long consumeTimes) {
        this.consumeTimes = consumeTimes;
    }

    public long getProductQty() {
        return productQty;
    }

    public void setProductQty(long productQty) {
        this.productQty = productQty;
    }

    public long getPrintProductQty() {
        return printProductQty;
    }

    public void setPrintProductQty(long printProductQty) {
        this.printProductQty = printProductQty;
    }
}
