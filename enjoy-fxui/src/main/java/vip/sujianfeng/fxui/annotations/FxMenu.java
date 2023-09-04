package vip.sujianfeng.fxui.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 挂载到菜单上的注解
 * 被注解类必须是FxBaseController的后代
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FxMenu {
    /**
     * 具体挂载在哪个菜单目录下
     * @return
     */
    String parentMenuId();

    /**
     * 菜单标题
     * @return
     */
    String title();

    /**
     * 绑定权限项，为空表示不受权限控制
     * @return
     */
    String per() default "";

    /**
     * 菜单图标
     * @return
     */
    String iconImg() default "";
}
