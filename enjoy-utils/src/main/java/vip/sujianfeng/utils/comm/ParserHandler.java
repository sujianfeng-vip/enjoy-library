package vip.sujianfeng.utils.comm;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.TargetError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by sujianfeng on 14-9-10.
 */
public class ParserHandler {
    private static Logger logger = LoggerFactory.getLogger(ParserHandler.class);
    public ParserHandler() throws Exception{
        this.getParams().put("parser", this);

        int today = DateTimeUtils.today2int();
        int year = today / 10000;
        int month = today / 100 % 100;
        int day = today % 100;

        this.getParams().put("today", today);
        this.getParams().put("year", year);
        this.getParams().put("month", month);
        this.getParams().put("day", day);
    }

    /**
     * 语法检查
     */
    private boolean check = false;

    /**
     * 解析过程的变量参数：存放modelConsole、conn、dbHelper...等初始化参数
     */
    private Map<String, Object> params = new HashMap<String, Object>();

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    /**
     * 暂存解析值，避免在同一次运行相同表达式被重复计算，注意：外部在正确的时机点清空暂存数据
     */
    private Map<String, Object> saveParserValues = new HashMap<String, Object>();

    public Map<String, Object> getSaveParserValues() {
        return saveParserValues;
    }

    public void setSaveParserValues(Map<String, Object> saveParserValues) {
        this.saveParserValues = saveParserValues;
    }


    public void addExtendParams(Map<String, Object> extendParams){
        if (extendParams != null){
            Set<Map.Entry<String,Object>> entrySet = extendParams.entrySet();
            for (Map.Entry<String, Object> entry : entrySet) {
                params.put(entry.getKey(),  entry.getValue());
            }
        }
    }

    public static void main(String[] args){
        Interpreter i = new Interpreter();
        StringBuilder script = new StringBuilder();
        script.append(" abc = 0; \n");
        script.append(" i = 1; v");
        script.append(" while(i <= 100){ \n");
        script.append("	abc += i; \n");
        script.append("	i++; \n");
        script.append(" }");
        script.append(" return 12345; \n");
        try {
            System.out.println(i.eval(script.toString()));
            System.out.println(i.get("abc"));
        } catch (EvalError e) {
            logger.error(e.toString(), e);
        }
    }

    /**
     * 将表达式里面的包括中括号的元素列出来
     * @param express
     */
    public static List<String> parseExpr2itemList(String express){
        return parseExpr2itemList(express, '[', ']');
    }

    public static List<String> parseExpr2itemList(String express, char begin, char end){
        List<String> itemList = new ArrayList<String>();
        boolean bBegin = false;
        String item = "";
        String expressTemp = express;
        for (int i = 0; i < expressTemp.length(); i++)
        {
            if (expressTemp.charAt(i) == end)
            {
                if (bBegin && !"".equals(item))
                {
                    itemList.add(item);
                    bBegin = false;
                    item = "";
                }
            }
            if (expressTemp.charAt(i) == begin)
                bBegin = true;
            else
            {
                if (bBegin)
                    item += expressTemp.charAt(i);
            }
        }
        return itemList;
    }

    public static List<String> parseExpr2itemList(String express, String begin, String end){
        List<String> itemList = new ArrayList<String>();
        int meetCount = 0;
        StringBuilder item = new StringBuilder();
        String expressTemp = express.toLowerCase();
        begin = begin.toLowerCase();
        end = end.toLowerCase();
        int i = 0;
        while (i < expressTemp.length()){
            String remainStr = expressTemp.substring(i);
            if (remainStr.indexOf(end) == 0){
                if (meetCount > 1){
                    item.append(end);
                }
                if (meetCount == 1 && item.length() > 0)
                {
                    itemList.add(item.toString());
                    meetCount = 0;
                    item = new StringBuilder();
                }else{
                    if (meetCount > 0){
                        meetCount--;
                    }
                }
                i += end.length();
            } if (remainStr.indexOf(begin) == 0){
                if (meetCount > 0)
                    item.append(begin);
                meetCount ++;
                i += begin.length();
            }else{
                if (meetCount > 0)
                    item.append(expressTemp.charAt(i));
                i++;
            }
        }
        return itemList;
    }

    private void addParams(Interpreter i, Map<String, Object> params) throws EvalError{
        if (params != null){
            Set<Map.Entry<String,Object>> entrySet = params.entrySet();
            for (Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator(); iterator.hasNext();) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iterator.next();
                String key = entry.getKey();
                Object value = entry.getValue();
                i.set(key, value);
            }
        }
    }

    /**
     * 解析脚本，并把结果返回
     * @param script
     * @return
     * @throws Exception
     */
    public Object parser(String script) throws Exception{
        if (script == null)
            return null;
        script = beforeParser(params, script);

		/*List<String> list = parseExpr2itemList(script);
		if (list.size() > 0){
			if (!check){
				throw new Exception(String.format("无法识别[%s], 请检查脚本", list.get(0)));
			}
			for (String item : list) {
				script = script.replace("[" + item + "]", "");
			}
		}*/

        Interpreter i = new Interpreter();
        addParams(i, params);

        Object result;
        if (saveParserValues.containsKey(script)){
            result = saveParserValues.get(script);
        }else{
            try {
                script = formatScript(script);
                //System.out.println("解析脚本：");
                //System.out.println(script);
                result = i.eval(script);
            } catch (Exception e) {
                if (e instanceof TargetError){
                    TargetError te = (TargetError)e;
                    if (te.getTarget() instanceof ParserException){
                        ParserException be = (ParserException)te.getTarget();
                        throw be;
                    }
                }
                throw new Exception(String.format("脚本:\n%s\n解析出错：%s", script, e.toString()));
            }

        }
        return result;
    }

    protected String beforeParser(Map<String, Object> params, String script) throws Exception {
        return script;
    }

    public static String translateByMap(String script, Map<String, Object> map){
        if (map.size() == 0){
            return script;
        }
        List<String> itemlist = parseExpr2itemList(script);
        for (String item : itemlist) {
            if (map.containsKey(item)){
                String parseItem = ConvertUtils.cStr(map.get(item));
                script = script.replace('[' + item + ']', parseItem);
            }
        }
        return script;
    }



    /**
     * 先将[xxx]替换为map值，在解析脚本
     * @param map
     * @param script
     * @return
     * @throws Exception
     */
    public Object parser(Map<String, Object> map, String script) throws Exception{
        script = translateByMap(script, map);
        return parser(script);
    }



    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    /**
     * 格式化脚本
     * @param text
     * @return
     */
    public static String formatScript(String text){
        if (StringUtilsEx.isEmpty(text))
            return "";
        text = text.trim();
        text = text.replace("\n", "");
        text = text.replace("\r", "");
        int unit = 5;
        int count = 0;
        boolean in = false; //是否在""内
        char last = ' ';
        StringBuilder sb = new StringBuilder();
        text = text.trim();
        while (text.length() > 0) {
            if (text.indexOf(";") == 0){
                sb.append(";").append("\n").append(StringUtilsEx.copyString(" ", count * unit));
                last = text.charAt(0);
                text = text.substring(1);
                continue;
            }
            if (text.indexOf("{") == 0){
                count++;
                sb.append("{").append("\n").append(StringUtilsEx.copyString(" ", count * unit));
                last = text.charAt(0);
                text = text.substring(1);
                continue;
            }
            if (text.indexOf("}") == 0){
                count--;
                sb.append("\n").append(StringUtilsEx.copyString(" ", count * unit)).append("}");
                last = text.charAt(0);
                text = text.substring(1);
                continue;
            }
            if (text.charAt(0) == '"'){
                in = true;
            }
            if (text.charAt(0) == '"'){
                in = false;
            }
            if (StringUtilsEx.isEmpty(text.charAt(0))){
                if (!StringUtilsEx.isEmpty(last)  //上一个字符不为空
                        || in //在""内
                        ){
                    sb.append(text.charAt(0));
                }
            }else{
                sb.append(text.charAt(0));
            }
            last = text.charAt(0);
            text = text.substring(1);
        }
        return sb.toString();
    }


    public void throwError(String error) throws ParserException{
        throw new ParserException(error);
    }

    public String cStr(Object value){
        return ConvertUtils.cStr(value);
    }

    public BigDecimal cFloat(Object value){
        return ConvertUtils.cFloat(value);
    }

    public int cInt(Object value){
        return ConvertUtils.cInt(value);
    }

    public boolean cBool(Object value){
        return ConvertUtils.cBool(value);
    }

    public long cLong(Object value){
        return ConvertUtils.cLong(value);
    }

    public String format(String text, Object[] args){
        return String.format(text, args);
    }

}
