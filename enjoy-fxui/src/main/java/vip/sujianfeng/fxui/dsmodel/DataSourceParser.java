package vip.sujianfeng.fxui.dsmodel;


import vip.sujianfeng.fxui.annotations.DsModel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author 苏建锋
 * @create 2019-09-17 19:43
 */
public class DataSourceParser {
    private DataSourceParser() {}
    private static Map<String, DataSourceModel> CACHE = new ConcurrentHashMap<>();

    private static DataSourceModel parseModel(String clsName){
        Class<?> aClass;
        try {
            aClass = Class.forName(clsName);
        } catch (ClassNotFoundException e) {
            return null;
        }
        DsModel dsModel = aClass.getAnnotation(DsModel.class);
        DataSourceModel result = new DataSourceModel();
        result.setTitle(dsModel.title());
        result.setEditControllerClass(dsModel.editControllerClass());
        result.setViewControllerClass(dsModel.viewControllerClass());
        result.setListControllerClass(dsModel.listControllerClass());
        return result;
    }

    public static DataSourceModel getDsModel(String clsName){
        DataSourceModel result = CACHE.get(clsName);
        if (result == null){
            result = parseModel(clsName);
            CACHE.put(clsName, result);
        }
        return result;
    }

}
