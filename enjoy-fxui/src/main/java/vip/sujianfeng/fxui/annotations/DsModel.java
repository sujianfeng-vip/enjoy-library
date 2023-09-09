package vip.sujianfeng.fxui.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 资源模型注解
 * author SuJianFeng
 * createTime  2019/9/17
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DsModel {

    String title();


    Class<?> uiControllerClass() default Object.class;


    Class<?> listControllerClass() default Object.class;

    Class<?> editControllerClass() default Object.class;

    Class<?> viewControllerClass() default Object.class;
}
