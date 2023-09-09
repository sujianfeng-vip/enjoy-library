package vip.sujianfeng.redis.mq;

/**
 * author SuJianFeng
 * createTime  2020/9/27 16:13
 **/
public class TempObj {
    private int id;
    private String name;

    public TempObj(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
