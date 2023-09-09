package vip.sujianfeng.fxui.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 脚本机器人功能注解
 * author SuJianFeng
 * createTime  2022/12/20
 * @Description
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JbRobotFunction {
    /**
     * 功能名称
     * @return
     */
    String value();

    /**
     * 输入参数描述
     * @return
     */
    String paramDesc() default "";

    /**
     * 返回结果描述
     * @return
     */
    String resultDesc() default "";

    /**
     * 功能描述
     * @return
     */
    String desc() default "";

    /**
     * 案例
     * @return
     */
    String example() default "";

}
