package vip.sujianfeng.fxui.forms.base;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import vip.sujianfeng.fxui.dsmodel.*;
import vip.sujianfeng.fxui.enums.OpType;
import vip.sujianfeng.fxui.interfaces.FxDiskDataHandler;
import vip.sujianfeng.fxui.utils.DialogUtils;
import vip.sujianfeng.fxui.utils.FxFormUtils;
import vip.sujianfeng.utils.comm.ConvertUtils;
import vip.sujianfeng.utils.define.CallResult;

/**
 * @author SuJianFeng
 * @date 2019/9/15 16:50
 **/
public abstract class FxListDataController<T extends FxBaseModel, P extends FxPageParam, D extends FxDiskDataHandler<T>> extends FxDsController<T, P, D> {
    /**
     * 分页控制
     */
    private FxPageRows<T> pageRows = new FxPageRows<>("", 1, 10);
    /**
     * 观察者模式-行数据
     */
    private ObservableList<T> rows = FXCollections.observableArrayList();

    abstract protected TableView<T> getTableView();



    final public void loadFirstPageRows(){
        this.getPageParam().setPageNo(1);
        loadPageRows();
    }

    final public void loadPriorPageRows(){
        if (pageRows.getPageIndex() <= 1){
            DialogUtils.info("已经是首页!");
            return;
        }
        this.getPageParam().setPageNo(this.getPageParam().getPageNo() - 1);
        loadPageRows();
    }

    final public void loadNextPageRows(){
        if (pageRows.getPageIndex() >= Math.ceil(pageRows.getTotalSize()*1.0 / pageRows.getPageSize())){
            DialogUtils.info("已经是尾页!");
            return;
        }
        this.getPageParam().setPageNo(this.getPageParam().getPageNo() + 1);
        loadPageRows();
    }

    final public void loadLastPageRows(){
        int totalPages = ConvertUtils.cInt(Math.ceil(pageRows.getTotalSize() * 1.0 / pageRows.getPageSize()));
        if (pageRows.getPageIndex() >= totalPages){
            DialogUtils.info("已经是尾页!");
            return;
        }
        this.getPageParam().setPageNo(totalPages);
        loadPageRows();
    }


    final public void loadPageRows() {
        rows.clear();
        CallResult<FxPageRows<T>> op = getDataHandler().queryPage(this.getPageParam());
        pageRows = op.getData();
        rows.addAll(pageRows.getRows());
        pageRows.setCondition("");
        afterLoadPageRows(pageRows);
        getTableView().setItems(rows);
        refreshForm();
    }

    protected void afterLoadPageRows(FxPageRows<T> pageRows) {

    }

    protected String getCondition(String condition){
        return condition;
    }

    protected String getOrderBy() {
        return "";
    }

    final public void export(){
        DialogUtils.info("开发中...");
    }

    protected void beforeAddNew(T masterData){

    }

    final public <C extends FxBaseController> void addNew(T masterData, Class<C> editControllerClass){
        masterData.setId(null);
        beforeAddNew(masterData);
        C controller = FxFormUtils.getController(editControllerClass, Modality.NONE, this);
        controller.putParams(OpType.AddNew, masterData);
        controller.show();
    }

    final public <C extends FxBaseController> void copyNew(T masterData, Class<C> editControllerClass){
        beforeAddNew(masterData);
        C controller = FxFormUtils.getController(editControllerClass, Modality.NONE, this);
        controller.putParams(OpType.copyNew, masterData);
        controller.show();
    }

    final public <C extends FxBaseController> void view(T masterData, Class<C> viewControllerClass){
        C controller = FxFormUtils.getController(viewControllerClass, Modality.NONE, this);
        controller.putParams(OpType.View, masterData);
        controller.show();
    }

    final public T getSelectRow(){
        return getTableView().getSelectionModel().getSelectedItem();
    }

    final public T getPriorRow(){
        if (getTableView().getItems().size() == 0){
            return null;
        }
        int selectedIndex = getTableView().getSelectionModel().getSelectedIndex();
        if (selectedIndex == 0){
            return null;
        }
        if (selectedIndex == -1){
            getTableView().getSelectionModel().select(0);
            return getSelectRow();
        }
        getTableView().getSelectionModel().select(selectedIndex - 1);
        return getSelectRow();
    }

    final public T getNextRow(){
        if (getTableView().getItems().size() == 0){
            return null;
        }
        int selectedIndex = getTableView().getSelectionModel().getSelectedIndex();
        if (selectedIndex == getTableView().getItems().size() - 1){
            return null;
        }
        if (selectedIndex == -1){
            getTableView().getSelectionModel().select(0);
            return getSelectRow();
        }
        if (getTableView().getItems().size() >= selectedIndex + 1){
            getTableView().getSelectionModel().select(selectedIndex + 1);
            return getSelectRow();
        }
        return null;
    }

    final protected <C extends FxBaseController> void edit(T masterData, Class<C> editControllerClass){
        C controller = FxFormUtils.getController(editControllerClass, Modality.NONE, this);
        controller.putParams(OpType.Edit, masterData);
    }

    final protected void delete(T masterData){
        DialogUtils.confirm("准备删除此笔数据，您确认要执行此操作吗?", "", (clickOk)->{
            if (clickOk) {
                FxManyIdParam param = new FxManyIdParam();
                param.getIds().add(masterData.getId());
                CallResult<Integer> op = getDataHandler().delete(param);
                if (!op.isSuccess()) {
                    DialogUtils.error(op.getMessage());
                    return;
                }
                this.loadPageRows();
            }
        });
    }

    final protected void refreshForm(){
        getTableView().refresh();
    }

    public FxPageRows<T> getPageRows() {
        return pageRows;
    }

    public ObservableList<T> getRows() {
        return rows;
    }

}
