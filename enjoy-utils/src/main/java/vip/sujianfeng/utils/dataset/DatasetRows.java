package vip.sujianfeng.utils.dataset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.sujianfeng.utils.comm.ConvertUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatasetRows<T> {

    private static Logger logger = LoggerFactory.getLogger(DatasetRows.class);

    private List<T> data;
    private Map<String, Field> fieldMap;
    private List<String> sortFields;

    public DatasetRows(List<T> data, List<String> sortFields) {
        this.data = data;
        this.fieldMap = new HashMap<>();
        this.sortFields = sortFields;
        if (this.data.size() > 0){
            getDeclaredField2map(this.data.get(0).getClass());
        }
        this.data.sort((o1, o2) -> {
            for (int i = 0; i < this.sortFields.size(); i++) {
                Object v1 = this.getFieldValue(o1, this.sortFields.get(i));
                Object v2 = this.getFieldValue(o2, this.sortFields.get(i));
                if (v1 == null && v2 != null){
                    return -1;
                }
                if (v1 != null && v2 == null){
                    return 1;
                }
                String sV1 = String.valueOf(v1);
                String sV2 = String.valueOf(v2);
                int compareValue = sV1.compareTo(sV2);
                if (compareValue != 0) {
                    return compareValue;
                }
            }
            return 0;
        });
    }

    public String getKeyValues(T row, List<String> bySortFields){
        List<String> keyList = bySortFields == null || bySortFields.size() == 0 ? this.sortFields : bySortFields;
        StringBuilder result = new StringBuilder();
        for (String sortField : keyList) {
            Object fieldValue = this.getFieldValue(row, sortField);
            result.append(String.format("%s,", fieldValue));
        }
        return result.toString();
    }


    private void getDeclaredField2map(Class<?> tmpClass){
        if (tmpClass == null){
            return;
        }
        Field[] temps = tmpClass.getDeclaredFields();
        for (Field field : temps){
            this.fieldMap.put(field.getName(), field);
        }
        getDeclaredField2map(tmpClass.getSuperclass());
    }

    protected Object getFieldValue(Object obj, String field) {
        if (obj instanceof Map) {
            return ((Map<?, ?>)obj).get(field);
        }
        Field f = this.fieldMap.get(field);
        if (f == null){
            return null;
        }
        try {
            f.setAccessible(true);
            return f.get(obj);
        } catch (IllegalAccessException e) {
            logger.error(e.toString());
            return null;
        }
    }

    protected void setFieldValue(Object obj, String field, Object value) {
        if (obj instanceof Map) {
            ((Map)obj).put(field, value);
            return;
        }
        Field f = this.fieldMap.get(field);
        if (f == null){
            return;
        }
        try {
            f.setAccessible(true);
            f.set(obj, value);
        } catch (IllegalAccessException e) {
            logger.error(e.toString());
        }
    }

    protected void addFieldValue(Object obj, String field, Object addValue) {
        try {
            Class<?> cls;
            if (obj instanceof Map) {
                Map<String, Object> map = ((Map)obj);
                Object oldValue = map.get(field);
                cls = oldValue != null ? oldValue.getClass() : addValue.getClass();
                map.put(field, addValue(cls, oldValue, addValue));
            }else {
                Field f = this.fieldMap.get(field);
                if (f == null){
                    return;
                }
                f.setAccessible(true);
                Object oldValue = f.get(obj);
                cls = f.getClass();
                f.set(obj, addValue(cls, oldValue, addValue));
            }
        } catch (IllegalAccessException e) {
            logger.error(e.toString());
        }
    }

    protected void maxFieldValue(Object obj, String field, Object value) {
        try {
            Class<?> cls;
            if (obj instanceof Map) {
                Map<String, Object> map = ((Map)obj);
                Object oldValue = map.get(field);
                cls = oldValue != null ? oldValue.getClass() : value.getClass();
                map.put(field, maxValue(cls, oldValue, value));
            }else {
                Field f = this.fieldMap.get(field);
                if (f == null){
                    return;
                }
                f.setAccessible(true);
                Object oldValue = f.get(obj);
                cls = f.getClass();
                f.set(obj, maxValue(cls, oldValue, value));
            }
        } catch (IllegalAccessException e) {
            logger.error(e.toString());
        }
    }

    protected void avgFieldValue(Object obj, String field, int count) {
        try {
            Class<?> cls;
            if (obj instanceof Map) {
                Map<String, Object> map = ((Map)obj);
                Object oldValue = map.get(field);
                cls = oldValue != null ? oldValue.getClass() : Integer.class;
                map.put(field, avgValue(cls, oldValue, count));
            }else {
                Field f = this.fieldMap.get(field);
                if (f == null){
                    return;
                }
                f.setAccessible(true);
                Object oldValue = f.get(obj);
                cls = f.getClass();
                f.set(obj, avgValue(cls, oldValue, count));
            }
        } catch (IllegalAccessException e) {
            logger.error(e.toString());
        }
    }

    protected void minFieldValue(Object obj, String field, Object value) {
        try {
            Class<?> cls;
            if (obj instanceof Map) {
                Map<String, Object> map = ((Map)obj);
                Object oldValue = map.get(field);
                cls = oldValue != null ? oldValue.getClass() : value.getClass();
                map.put(field, minValue(cls, oldValue, value));
            }else {
                Field f = this.fieldMap.get(field);
                if (f == null){
                    return;
                }
                f.setAccessible(true);
                Object oldValue = f.get(obj);
                cls = f.getClass();
                f.set(obj, minValue(cls, oldValue, value));
            }
        } catch (IllegalAccessException e) {
            logger.error(e.toString());
        }
    }

    private Object addValue(Class<?> cls, Object oldValue, Object addValue) {
        if (isAssignableFrom(cls, Integer.class)){
            return ConvertUtils.cInt(oldValue) + ConvertUtils.cInt(addValue);
        }
        if (isAssignableFrom(cls, Number.class)){
            double v = ConvertUtils.cDouble(oldValue);
            v = v + ConvertUtils.cDouble(addValue);
            return v;
        }
        BigDecimal v = ConvertUtils.cFloat(oldValue);
        return v.add(ConvertUtils.cFloat(addValue));
    }

    private Object avgValue(Class<?> cls, Object oldValue, int count) {
        if (isAssignableFrom(cls, Integer.class)){
            return ConvertUtils.cInt(oldValue) / count;
        }
        if (isAssignableFrom(cls, Number.class)){
            double v = ConvertUtils.cDouble(oldValue);
            return v / count;
        }
        BigDecimal v = ConvertUtils.cFloat(oldValue);
        return v.divide(ConvertUtils.cFloat(count), RoundingMode.HALF_UP);
    }

    private Object maxValue(Class<?> cls, Object oldValue, Object newValue) {
        if (isAssignableFrom(cls, Integer.class)){
            return oldValue != null && ConvertUtils.cInt(oldValue) > ConvertUtils.cInt(newValue) ? oldValue : newValue;
        }
        if (isAssignableFrom(cls, Number.class)){
            return oldValue != null && ConvertUtils.cDouble(oldValue) > ConvertUtils.cDouble(newValue) ? oldValue : newValue;
        }
        return oldValue != null && ConvertUtils.cFloat(oldValue).compareTo(ConvertUtils.cFloat(newValue)) > 0 ? oldValue : newValue;
    }

    private Object minValue(Class<?> cls, Object oldValue, Object newValue) {
        if (isAssignableFrom(cls, Integer.class)){
            return oldValue != null && ConvertUtils.cInt(oldValue) < ConvertUtils.cInt(newValue) ? oldValue : newValue;
        }
        if (isAssignableFrom(cls, Number.class)){
            return oldValue != null && ConvertUtils.cDouble(oldValue) < ConvertUtils.cDouble(newValue) ? oldValue : newValue;
        }
        return oldValue != null && ConvertUtils.cFloat(oldValue).compareTo(ConvertUtils.cFloat(newValue)) < 0 ? oldValue : newValue;
    }

    public static boolean isAssignableFrom(Class<?> aClass, Class<?> parent){
        if (aClass == null) {
            return false;
        }
        if (aClass.isAssignableFrom(parent)){
            return true;
        }
        return isAssignableFrom(aClass.getSuperclass(), parent);
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Map<String, Field> getFieldMap() {
        return fieldMap;
    }

    public void setFieldMap(Map<String, Field> fieldMap) {
        this.fieldMap = fieldMap;
    }

    public List<String> getSortFields() {
        return sortFields;
    }

    public void setSortFields(List<String> sortFields) {
        this.sortFields = sortFields;
    }
}
