package vip.sujianfeng.engine;
import com.alibaba.fastjson.JSON;
import vip.sujianfeng.utils.define.CallResult;

public class JscriptRunnerTest {

    public static void main(String[] args) {
        new JscriptRunnerTest().test3();
    }


    public void test1(){
        CallResult<String> callResult = new CallResult<>();
        String scriptJs = "function doTest(){" +
                "   return {" +
                "       simpleValue1: 1," +
                "       simpleValue2: '文本22'," +
                "       testObj: {a:1, b:2, c:3}," +
                "       testArr: [{x:1, y:2}, {x:11, y:22}]" +
                "   }; " +
                "}";
        JscriptRunner jscriptRunner = JscriptRunner.buildRunner(callResult, scriptJs);
        Object result = jscriptRunner.invokeFunctionAndConvertJson(callResult, "doTest");
        System.out.println(JSON.toJSONString(result));

    }

    public void test2(){
        CallResult<String> callResult = new CallResult<>();
        String scriptJs = "function doTest(){" +
                "   return [{" +
                "       testObj: {a:1, b:2, c:3}," +
                "       testArr: [{x:1, y:2}, {x:11, y:22}]" +
                "   }, {testObj: {a: 222}, testArr: [1,2,3] }]; " +
                "}";
        JscriptRunner jscriptRunner = JscriptRunner.buildRunner(callResult, scriptJs);
        Object result = jscriptRunner.invokeFunctionAndConvertJson(callResult, "doTest");
        System.out.println(JSON.toJSONString(result));

    }

    public void test3() {
        CallResult<String> callResult = new CallResult<>();
        String scriptJs = "function doTest(){" +
                " op.error('错误信息123');" +
                "}";
        JscriptRunner jscriptRunner = JscriptRunner.buildRunner(callResult, scriptJs);
        jscriptRunner.getBindings().put("op", callResult);
        jscriptRunner.invokeFunctionAndConvertJson(callResult, "doTest");
        System.out.println(JSON.toJSONString(callResult));
    }



}
