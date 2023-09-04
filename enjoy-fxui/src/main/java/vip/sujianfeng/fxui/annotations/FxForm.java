package vip.sujianfeng.fxui.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author SuJianFeng
 * @Date 2022/12/16
 * @Description
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FxForm {
    /**
     * xml资源文件地址，例如 /abc.fxml
     * @return
     */
    String value();

    /**
     * 标题
     * @return
     */
    String title() default "";
}
