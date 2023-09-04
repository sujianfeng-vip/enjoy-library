package vip.sujianfeng.fxui.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 资源模型注解
 * @Author SuJianFeng
 * @Date 2019/9/17
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DsModel {
    /**
     * 标题
     * @return
     */
    String title();

    /**
     * 默认页面控制类
     * @return
     */
    Class<?> uiControllerClass() default Object.class;

    /**
     * 列表页面控制类
     * @return
     */
    Class<?> listControllerClass() default Object.class;

    /**
     * 编辑页面控制类
     * @return
     */
    Class<?> editControllerClass() default Object.class;

    /**
     * 浏览页面控制类
     * @return
     */
    Class<?> viewControllerClass() default Object.class;
}
