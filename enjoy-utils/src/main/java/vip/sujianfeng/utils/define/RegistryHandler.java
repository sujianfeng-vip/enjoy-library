package vip.sujianfeng.utils.define;

import java.util.prefs.Preferences;

/**
 * @Author SuJianFeng
 * @Date 2023/1/29
 * @Description
 * Preferences.userRoot()
 * 操作的固定位置：\HKEY_CURRENT_USER\Software\JavaSoft\Prefs\
 **/
public class RegistryHandler {

    private String root;

    public RegistryHandler(String root) {
        this.root = root;
    }

    public String read(String key, String defaultValue) {
        Preferences p = Preferences.userRoot().node(root);
        return p.get(key, defaultValue);
    }

    public void write(String key, String value) {
        Preferences p = Preferences.userRoot().node(root);
        p.put(key, value);
    }


    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }
}
