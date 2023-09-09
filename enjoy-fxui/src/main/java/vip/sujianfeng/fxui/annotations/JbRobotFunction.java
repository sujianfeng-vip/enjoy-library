package vip.sujianfeng.fxui.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**

 * author SuJianFeng
 * createTime  2022/12/20
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JbRobotFunction {

    String value();

    String paramDesc() default "";

    String resultDesc() default "";

    String desc() default "";

    String example() default "";

}
