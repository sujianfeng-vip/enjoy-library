package vip.sujianfeng.fxui.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import vip.sujianfeng.fxui.annotations.FxForm;

import java.io.IOException;
import java.net.URL;

/**
 * @Author SuJianFeng
 * @Date 2022/12/16
 * @Description
 **/
public class FXmlUtils {

    /**
     * 加载FXML资源
     * @param fxmlUrl
     * @return
     * @throws IOException
     */
    public static Pane loadFXML(String fxmlUrl) throws IOException {
        URL url = FXmlUtils.class.getResource(fxmlUrl);
        return loadFXML(url);
    }

    public static Pane loadFXML(URL url) throws IOException {
        return getLoader(url).load();
    }

    public static FXMLLoader getLoader(Class<?> t) {
        return getLoader(getFXML(t));
    }

    public static FxForm getFXML(Class<?> t) {
        FxForm fxForm = t.getAnnotation(FxForm.class);
        if (fxForm == null) {
            throw new RuntimeException(String.format("%s not set @FXML!", t.getName()));
        }
        return fxForm;
    }

    public static FXMLLoader getLoader(FxForm fxForm) {
        URL url = FxFormUtils.class.getResource(fxForm.value());
        if (url == null) {
            throw new RuntimeException(String.format("not found fxml: %s", fxForm.value()));
        }
        return getLoader(url);
    }

    public static FXMLLoader getLoader(String fxmlUrl) {
        URL url = FXmlUtils.class.getResource(fxmlUrl);
        return getLoader(url);
    }

    public static FXMLLoader getLoader(URL url) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(url);
        return loader;
    }

}
