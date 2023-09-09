package vip.sujianfeng.fxui.dsmodel;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.sujianfeng.utils.comm.ReflectUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * author SuJianFeng
 * createTime  2022/11/4
 * @Description
 **/
public class FxBaseModel {
    private List<Field> fields = null;
    private String id;
    //数据状态：0-正常，1-删除
    private int state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Field> allDeclaredFields() {
        if (fields != null) {
            return fields;
        }
        fields = new ArrayList<>();
        Class<?> t = this.getClass();
        while (t != null) {
            fields.addAll(Arrays.asList(t.getDeclaredFields()));
            t = t.getSuperclass();
        }
        return fields;
    }

    public <T extends FxBaseModel> T clone(Class<T> t) {
        String json = JSON.toJSONString(this);
        return JSON.parseObject(json, t);
    }

    public Field getDeclaredField(String fieldName){
        return ReflectUtils.getDeclaredField(this.getClass(), fieldName);
    }

    public Object getFieldValue(String fieldName) {
        try {
            return ReflectUtils.getFieldValue(this, fieldName);
        } catch (IllegalAccessException e) {
            logger.error(e.toString(), e);
        }
        return null;
    }

    public Object getFieldValue(Field field) {
        try {
            return ReflectUtils.getFieldValue(this, field);
        } catch (IllegalAccessException e) {
            logger.error(e.toString(), e);
        }
        return null;
    }

    public void setFieldValue(String fieldName, Object value) {
        try {
            ReflectUtils.setFieldValue(this, fieldName, value);
        } catch (IllegalAccessException e) {
            logger.error(e.toString(), e);
        }
    }

    public void setFieldValue(Field field, Object value) {
        try {
            ReflectUtils.setFieldValue(this, field, value);
        } catch (IllegalAccessException e) {
            logger.error(e.toString(), e);
        }
    }

    private static Logger logger = LoggerFactory.getLogger(FxBaseModel.class);

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
