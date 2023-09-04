package vip.sujianfeng.fxui.ctrls;

import javafx.stage.Modality;
import vip.sujianfeng.fxui.forms.base.FxBaseController;
import vip.sujianfeng.fxui.utils.FxFormUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import vip.sujianfeng.utils.comm.StringUtilsEx;

/**
 * @author SuJianFeng
 * @date 2019/9/14 21:46
 * Tab页签操作类
 **/
public class TabPaneOp {

    private TabPane tabPane;

    public TabPaneOp(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    public <T extends FxBaseController> T showTabForm(Class<T> cls, Object... params){
        return showTabForm("", "", cls, params);
    }

    public <T extends FxBaseController> T showTabForm(String key, String title, Class<T> cls, Object... params){
        T controller = FxFormUtils.getController(key, cls, Modality.NONE, null, params);
        assert controller != null;
        if (StringUtilsEx.isEmpty(title)) {
            title = cls.getName();
        }
        controller.getStage().setTitle(title);
        controller.setContainer(this);
        if (StringUtilsEx.isEmpty(key)) {
            key = cls.getName();
        }
        Tab tab = this.getTab(key);
        if (tab == null){
            tab = this.addTab(key, title, controller.getStage().getScene().getRoot(), true);
        }
        this.selectTab(tab);
        return controller;
    }

    /**
     * 关闭tab
     */
    private static class TabCloseEvent implements EventHandler<Event> {
        private final Tab tab;
        private String key;
        public TabCloseEvent(Tab tab, String key) {
            this.tab = tab;
            this.key = key;
        }
        @Override
        public void handle(Event event) {
            FxBaseController controller = FxFormUtils.getController(key);
            if (controller != null) {
                controller.closeForm(true);
            }
        }
        public Tab getTab() {
            return tab;
        }

        public String getKey() {
            return key;
        }
    }

    public Tab addTab(String id, String title, Node content, boolean closeable){
        Tab tab = new Tab(title, content);
        tab.setId(id);
        tab.setClosable(closeable);
        this.tabPane.getTabs().add(tab);
        tab.setOnClosed(new TabCloseEvent(tab, id));
        return tab;
    }

    public void selectTab(int index){
        this.tabPane.getSelectionModel().select(index);
    }

    public Tab getTab(int index){
        return this.tabPane.getTabs().get(index);
    }

    public Tab getTab(String id){
        for (Tab tab : this.tabPane.getTabs()) {
            if (StringUtilsEx.sameText(tab.getId(), id)){
                return tab;
            }
        }
        return null;
    }

    public void selectTab(String id){
        Tab tab = getTab(id);
        if (tab != null){
            selectTab(tab);
        }
    }

    public void selectTab(Tab tab){
        this.tabPane.getSelectionModel().select(tab);
    }

    public int getTabIndex(String id){
        for (int i = 0; i < this.tabPane.getTabs().size(); i++) {
            if (StringUtilsEx.sameText(this.tabPane.getTabs().get(i).getId(), id)){
                return i;
            }
        }
        return -1;
    }

    public void removeTab(Class<?> cls) {
        removeTab(cls.getName());
    }

    public void removeTab(String id){
        int tabIndex = getTabIndex(id);
        if (tabIndex != -1){
            this.tabPane.getTabs().remove(tabIndex);
        }
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public void setTabPane(TabPane tabPane) {
        this.tabPane = tabPane;
    }
}
