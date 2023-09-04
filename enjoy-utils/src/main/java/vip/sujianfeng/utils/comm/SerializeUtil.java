package vip.sujianfeng.utils.comm;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by pilot on 2017/7/13.
 */
public class SerializeUtil {
    private static Logger logger = LoggerFactory.getLogger(SerializeUtil.class);
    public static byte[] serialize(Object object) {
        if (object == null){
            return null;
        }
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return null;
    }

    public static Object unSerialize(byte[] bytes) {
        if (bytes == null){
            return null;
        }
        ByteArrayInputStream bais = null;
        try {
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return null;
    }

    public static void main(String[] args){
        /*
        WeixinTbToken oriObj = new WeixinTbToken();
        oriObj.setTbToken("张三");
        oriObj.setWeixinUserId(123);
        List<WeixinTbToken> rows = new ArrayList<>();
        rows.add(oriObj);
        byte[] data = serialize(rows);
        Object obj = unSerialize(data);
        System.out.println(obj.getClass());
        ScoreGift scoreGift = new ScoreGift();
        byte[] bytes = serialize(scoreGift);
        unSerialize(bytes);
        */



    }

}
