package vip.sujianfeng.utils.define;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author SuJianFeng
 * @date 2019/11/29 17:02
 * 接口调用结果对象
 **/
public class CallResult<T> {

    private static Logger logger = LoggerFactory.getLogger(CallResult.class);

    public static final int SUCCESS_CODE = 2000;
    public static final int NOT_LOGGED_CODE = 3000;
    public static final int SYSTEM_ERROR_CODE = 4000;
    public static final int BIZ_ERROR_CODE = 5000;

    private int code = SUCCESS_CODE;
    private String message = "";
    /**
     * 扩展属性
     */
    private Map<String, Object> attributes = new HashMap<>();
    private T data;

    public <C> CallResult<C> cloneTo(Class<C> t, CallResult<C> result) {
        result.code = this.code;
        result.message = this.message;
        result.attributes.putAll(this.attributes);
        if (this.data != null) {
            result.data = JSON.parseObject(JSON.toJSONString(this.data), t);
        }
        return result;
    }

    public <C> CallResult<C> clone(Class<C> t) {
        return cloneTo(t, new CallResult<>());
    }

    public <C> CallResult<List<C>> cloneListTo(Class<C> t, CallResult<List<C>> result) {
        result.code = this.code;
        result.message = this.message;
        result.attributes.putAll(this.attributes);
        if (this.data != null) {
            result.data = JSON.parseArray(JSON.toJSONString(this.data), t);
        }
        return result;
    }

    public <C> CallResult<List<C>> cloneList(Class<C> t) {
        return cloneListTo(t, new CallResult<>());
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public interface Op<T>{
        void opCall(CallResult<T> httpResult) throws Exception;
    }

    public static <T> CallResult<T> opCall(CallResult.Op<T> op){
        CallResult<T> httpResult = new CallResult<>();
        try{
            op.opCall(httpResult);
        }catch (Exception e){
            logger.error(e.toString(), e);
            httpResult.putCode(SYSTEM_ERROR_CODE, e.toString().replace("java.lang.Exception:", ""));
        }
        return httpResult;
    }


    public CallResult<T> success(String message){
        return putCode(SUCCESS_CODE, message);
    }

    public CallResult<T> success(String message, Object... params){
        if (params.length > 0){
            message = String.format(message, params);
        }
        return putCode(SUCCESS_CODE, message);
    }

    public CallResult<T> error(String message){
        return putCode(BIZ_ERROR_CODE, message);
    }

    public CallResult<T> error(String message, Object... params){
        if (params.length > 0){
            message = String.format(message, params);
        }
        return putCode(BIZ_ERROR_CODE, message);
    }

    public CallResult<T> putCode(int code, String message){
        this.code = code;
        this.message = message;
        return this;
    }

    public static <T> CallResult<T> parse(String json, Class<T> t) {
        CallResult<?> tmp = JSON.parseObject(json, CallResult.class);
        return tmp.clone(t);
    }

    public boolean isSuccess(){
        return this.code == SUCCESS_CODE;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static void main(String[] args) {
        CallResult<String> callResult = new CallResult<>();
        CallResult.opCall(op->{
            op.setData("aa");
        });
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
