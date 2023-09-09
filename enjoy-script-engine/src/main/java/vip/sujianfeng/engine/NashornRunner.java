package vip.sujianfeng.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.sujianfeng.utils.define.CallResult;

import javax.script.*;

/**
 * author SuJianFeng
 * createTime  2019/5/22 10:19
 * js解析引擎
 **/
public class NashornRunner {

    private static Logger logger = LoggerFactory.getLogger(NashornRunner.class);

    private ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

    private String script;

    public NashornRunner(String script){
        this.script = script;
    }

    public ScriptEngine getEngine(){
        return engine;
    }

    public Object eval(CallResult<?> callResult) {
        try {
            return getEngine().eval(script);
        } catch (ScriptException e) {
            logger.error("\n=======脚本解析出错=========\n{}\n错误信息：{}\n================\n",
                    script, e.toString());
            logger.error(e.toString(), e);
            callResult.error(e.toString());
        }
        return null;
    }

    public Object eval(CallResult<?> callResult, SimpleBindings params) {
        try {
            return getEngine().eval(script, params);
        } catch (ScriptException e) {
            logger.error("\n=======脚本解析出错=========\n{}\n错误信息：{}\n================\n",
                    script, e.toString());
            logger.error(e.toString(), e);
            callResult.error(e.toString());
        }
        return null;
    }

    public Object invokeFunction(CallResult<?> callResult, String funcName, Object... params){
        //ScriptEngine engine = getEngine();
        try {
            //engine.eval(script);
            Invocable in = (Invocable) engine;
            return in.invokeFunction(funcName, params);
        } catch (ScriptException | NoSuchMethodException e) {
            logger.error("\n=======脚本解析出错=========\n{}\n错误信息：{}\n================\n",
                    script, e.toString());
            logger.error(e.toString(), e);
            callResult.error(e.toString());
        }
        return null;
    }

    public String getScript() {
        return script;
    }
}
