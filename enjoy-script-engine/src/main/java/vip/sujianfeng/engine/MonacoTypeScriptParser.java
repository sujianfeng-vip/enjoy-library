package vip.sujianfeng.engine;

import vip.sujianfeng.utils.comm.StringBuilderEx;
import vip.sujianfeng.utils.comm.StringUtilsEx;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * author SuJianFeng
 * createTime  2019/12/5 14:48
 **/
public class MonacoTypeScriptParser {


    public void parseClass(Class<?> classz, StringBuilderEx sb, String className) {
        if (StringUtilsEx.isEmpty(className)){
            className = classz.getSimpleName();
        }
        sb.append("declare class ").append(className).appendRow(" { ");
        parseFunctions(classz.getDeclaredMethods(), sb, false);
        sb.appendRow("}");
    }

    public void parseInstance(Class<?> classz, StringBuilderEx sb, String className) {
        sb.append("declare class ").append(className).appendRow(" { ");
        while (classz != null){
            parseFunctions(classz.getDeclaredMethods(), sb, true);
            classz = classz.getSuperclass();
        }
        sb.appendRow("}");
    }

    public void parseMap(Map<String, String> map , StringBuilderEx sb, String className) {
        sb.append("declare enum ").append(className).appendRow(" { ");

        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append("\t").appendRow(entry.getKey());
        }
        sb.appendRow("}");
    }

    private void parseFunctions(Method[] methods, StringBuilderEx sb, Boolean instance) {
        int size = methods.length;
        for (int i = 0; i < size; i++) {
            Method method = methods[i];
            parseFunction(method, sb, instance);
            if (size > 1 && i < size - 1){
                sb.appendRow(",");
            }
            sb.appendRow("");
        }
    }

    private void parseFunction(Method method, StringBuilderEx sb, Boolean instance) {
        int modifiers = method.getModifiers();
        sb.append("\t");
        if (Modifier.isStatic(modifiers) || instance) {
            sb.append("static ");
        }
        sb.append(method.getName()).append("(");
        parseFunctionParams(method, sb);
        sb.append(")");
        parseFunctionReturnType(method, sb);
    }

    private void parseFunctionParams(Method method, StringBuilderEx sb) {
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter  = parameters[i];
            if (i > 0){
                sb.append(" , ");
            }
            sb.append(parameter.getName()).append(":").append(javaType2JavascriptType(parameter.getType().getSimpleName()));
        }
    }

    private void parseFunctionReturnType(Method method, StringBuilderEx sb) {
        String returnType = javaType2JavascriptType(method.getReturnType().getSimpleName());
        if (StringUtilsEx.isNotEmpty(returnType)) {
            sb.append(":").append(returnType);
        }
    }

    private String javaType2JavascriptType(String typeName){
        typeName = typeName != null ? typeName.toLowerCase() : "";
        if (typeName.equalsIgnoreCase("string")) return "string";
        if (typeName.equalsIgnoreCase("object")) return "Object";
        if (typeName.equalsIgnoreCase("object[]")) return "object[]";
        if (typeName.equalsIgnoreCase("void")) return "";
        if (typeName.equalsIgnoreCase("int")) return "number";
        if (typeName.equalsIgnoreCase("int[]")) return "number";
        if (typeName.equalsIgnoreCase("list")) return "Object[]";
        if (typeName.equalsIgnoreCase("map")) return "Object";
        if (typeName.equalsIgnoreCase("jsrunnable")) return "function";
        if (typeName.equalsIgnoreCase("dbapi")) return "DBApi";
        if (typeName.equalsIgnoreCase("opresult")) return "OpResult";
        if (typeName.equalsIgnoreCase("date")) return "java.util.Date";
        return typeName;
    }
}
