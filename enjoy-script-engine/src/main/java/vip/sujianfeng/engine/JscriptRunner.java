package vip.sujianfeng.engine;

import vip.sujianfeng.engine.utils.ScriptObjectConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.sujianfeng.utils.comm.MD5Util;
import vip.sujianfeng.utils.comm.StringBuilderEx;
import vip.sujianfeng.utils.define.CallResult;

import javax.script.ScriptContext;
import javax.script.SimpleBindings;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * author SuJianFeng
 * createTime  2019/6/25 14:32
 **/
public class JscriptRunner {

    private static Logger logger = LoggerFactory.getLogger(JscriptRunner.class);
    private NashornRunner nashornRunner;
    private String currScriptJs;
    private static MonacoTypeScriptParser monacoTsParser = new MonacoTypeScriptParser();
    private static Map<String, JscriptRunner> RUNNER = new ConcurrentHashMap<>();

    public static JscriptRunner buildRunner(CallResult<?> callResult, String scriptJS) {
        String key = MD5Util.MD5Encoder(scriptJS, "UTF-8");
        JscriptRunner runner = RUNNER.get(key);
        if (runner == null){
            runner = new JscriptRunner();
            // logger.info("\n=======\n创建新的脚本运行器->\nscriptJS -> {}\n=======\n", scriptJS);
            runner.currScriptJs = scriptJS;
            runner.nashornRunner = new NashornRunner(scriptJS);
            //注册Java对象 -> 用于在js中调用
            runner.bindings.put("LOG", logger);
            runner.nashornRunner.getEngine().setBindings(runner.bindings, ScriptContext.GLOBAL_SCOPE);
            runner.nashornRunner.eval(callResult);
            if (callResult.isSuccess()){
                logger.error(callResult.getMessage());
                runner.bindings.put("callResult", callResult);
                RUNNER.put(key, runner);
            }
            //静态方法调用方式：
            // var DateUtils = Java.type('cc.twobears.tbcore.utils.DateTimeUtils');
            // DateUtils.int2show(20201212);
        }
        return runner;
    }

    private SimpleBindings bindings = new SimpleBindings();

    /**
     * 禁止构造
     */
    private JscriptRunner(){
        this.timeoutSeconds = 8;
    }

    /**
     * 超时时间（单位秒）
     */
    private int timeoutSeconds;


    /**
     * java对象转换为js声明，用于monaco的js语法提示
     * @return
     */
    public String extraLanguage(){
        StringBuilderEx sb = new StringBuilderEx();
        for (Map.Entry<String, Object> entry : this.bindings.entrySet()) {
            monacoTsParser.parseInstance(entry.getValue().getClass(), sb, entry.getKey());
        }
        return sb.toString();
    }

    public Object invokeFunction(CallResult<?> callResult, String func, Object... params) {
        bindings.put("callResult", callResult);
        Callable<Object> callableThread = () -> {
            Object result = nashornRunner.invokeFunction(callResult, func, params);
            return result;
        };
        FutureTask<Object> task = new FutureTask<>(callableThread);
        // 开启线程
        new Thread(task).start();
        try {
            // 如果8秒没有返回值就抛出异常
            return task.get(this.timeoutSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            String errMsg = String.format("执行脚本异常:%s", e.toString());
            logger.error(errMsg, e);
            callResult.error(errMsg);
            return null;
        }
    }

    public Object invokeFunctionAndConvertJson(CallResult<?> callResult, String func, Object... params) {
        Object result = invokeFunction(callResult, func, params);
        result = ScriptObjectConvertUtils.convert2json(result);
        return result;
    }

    public String getCurrScriptJs() {
        return currScriptJs;
    }

    public SimpleBindings getBindings() {
        return bindings;
    }

    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(int timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }
}
