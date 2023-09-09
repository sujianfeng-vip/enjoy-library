package vip.sujianfeng.fxui.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 脚本机器人
 * author SuJianFeng
 * createTime  2022/12/20
 * @Description
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JbRobot {
    /**
     * 功能名称
     * @return
     */
    String value();

    /**
     * 实例代码
     * @return
     */
    String code();

    /**
     * 功能描述
     * @return
     */
    String desc() default "";

}
