package vip.sujianfeng.utils.comm;

/**
 * @author SuJianFeng
 * @date 2019/9/10 13:36
 * 模仿kotlin的when语法
 **/
public class When {

    private Object item;
    private boolean ok = false;

    public interface Then{
        void then();
    }

    public static When the(Object item){
        When the = new When();
        the.item = item;
        return the;
    }

    public When when(Object value, Then then){
        if (this.ok){
            return this;
        }
        if (item != null && item.equals(value)){
            this.ok = true;
            then.then();
        }
        return this;
    }

    public void other(Then then){
        if (!this.ok){
            then.then();
        }
    }

    public Object getItem() {
        return item;
    }

    public void setItem(Object item) {
        this.item = item;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }
}
