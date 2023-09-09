package vip.sujianfeng.fxui.forms.base;

import vip.sujianfeng.fxui.ctrls.FxCtrlDataBindHandler;
import vip.sujianfeng.fxui.dsmodel.FxBaseModel;
import vip.sujianfeng.fxui.dsmodel.FxManyIdParam;
import vip.sujianfeng.fxui.dsmodel.FxPageParam;
import vip.sujianfeng.fxui.enums.FxFormState;
import vip.sujianfeng.fxui.enums.OpType;
import vip.sujianfeng.fxui.interfaces.FxDiskDataHandler;
import vip.sujianfeng.fxui.utils.DialogUtils;
import vip.sujianfeng.utils.comm.StringUtilsEx;
import vip.sujianfeng.utils.define.CallResult;


/**
 * author SuJianFeng
 * createTime  2019/9/15 17:28
 **/
public abstract class FxMasterDataController<T extends FxBaseModel, P extends FxPageParam, D extends FxDiskDataHandler<T>> extends FxDsController<T, P, D> {

    private OpType opType;
    private FxFormState formState;


    private T oldMasterData;
    private T masterData;

    private FxCtrlDataBindHandler<T> bindHandler;

    public void locked() {
        if (this.bindHandler != null) {
            this.bindHandler.setLocked(true);
        }
    }

    public void unLocked() {
        if (this.bindHandler != null) {
            this.bindHandler.setLocked(false);
        }
    }

    protected void loadData(OpType opType, T masterData) {
        this.opType = opType;
        this.formState = FxFormState.Viewing;
        if (opType == OpType.View){
            this.oldMasterData = masterData;
            this.masterData = masterData;
        }
        if (opType == OpType.AddNew){
            this.formState = FxFormState.Adding;
            this.oldMasterData = null;
            this.masterData = masterData.clone(this.getModelClass());
        }
        if (opType == OpType.copyNew){
            this.formState = FxFormState.Adding;
            this.oldMasterData = null;
            this.masterData = masterData.clone(this.getModelClass());
            this.masterData.setId(null);
        }
        if (opType == OpType.Edit){
            this.formState = FxFormState.Editing;
            this.oldMasterData = masterData.clone(this.getModelClass());
            this.masterData = masterData;
        }
        if (this.bindHandler == null) {
            this.bindHandler = new FxCtrlDataBindHandler<>(this.getStage());
        }
        this.bindHandler.updateModelObj(this.masterData);
        afterInitData();
    }
    
    protected void afterInitData(){
        
    }

    @Override
    public void loadPageData(Object... params) {
        super.loadPageData(params);
        this.opType = params.length > 0 ? (OpType) params[0] : null;
        this.loadData(opType, params.length > 1 ? (T) params[1] : null);
    }

    protected void beforeSave(CallResult<?> callResult, T oldMasterData, T masterData) throws Exception{

    }

    /**
     * 保存单据数据
     */
    final protected void save(Class<?> viewControllerClass) throws Exception {
        FxFormState oriFormState = this.formState;
        if (oriFormState != FxFormState.Adding && oriFormState != FxFormState.Editing){
            throw new Exception("窗体不在新增或编辑状态，无法执行保存操作!");
        }
        CallResult<?> callResult = new CallResult<>();
        beforeSave(callResult, oldMasterData, masterData);
        if (!callResult.isSuccess()) {
            DialogUtils.error(callResult.getMessage());
            return;
        }
        this.setFormState(FxFormState.Saving);
        CallResult<T> op;
        if (StringUtilsEx.isEmpty(masterData.getId())) {
            op = getDataHandler().add(masterData);
        } else {
            op = getDataHandler().update(masterData);
        }
        if (op.isSuccess()) {
            //执行成功后
            this.setFormState(FxFormState.Viewing);
            afterSave(oldMasterData, masterData, viewControllerClass);
        }else {
            DialogUtils.error(op.getMessage());
            //执行失败后
            this.setFormState(oriFormState); //还原之前状态
        }
    }

    protected void afterSave(T oldMasterData, T masterData, Class<?> viewControllerClass) throws Exception{
        this.closeForm();
        Object parentController = getParent();
        if (parentController instanceof FxListDataController){
            FxListDataController listCtrl = (FxListDataController) parentController;
            if (getOpType() == OpType.AddNew || getOpType() == OpType.copyNew){
                listCtrl.loadFirstPageRows(); //重新加载数据
            }else{
                listCtrl.refreshForm(); //刷新列表页面
            }
            if (viewControllerClass == null) {
                this.closeForm();
                return;
            }
            listCtrl.view(masterData, viewControllerClass);
        }
    }

    final protected void gotoPrior(){
        if (this.formState != FxFormState.Viewing){
            DialogUtils.error("当前窗体不在浏览状态，无法执行此操作!");
            return;
        }
        Object parent = getParent();
        if (parent instanceof FxListDataController){
            FxListDataController<T, P, D> fxList = (FxListDataController) parent;
            T item = fxList.getPriorRow();
            if (item == null){
                DialogUtils.error("当前列表已没有上一笔数据了!");
                return;
            }
            loadData(OpType.View, item);
        }
    }

    final protected void gotoNext(){
        if (this.formState != FxFormState.Viewing){
            DialogUtils.error("当前窗体不在浏览状态，无法执行此操作!");
            return;
        }
        Object parent = getParent();
        if (parent instanceof FxListDataController){
            FxListDataController<T, P, D> fxList = (FxListDataController) parent;
            T item = fxList.getNextRow();
            if (item == null){
                DialogUtils.error("当前列表已没有下一笔数据了!");
                return;
            }
            loadData(OpType.View, item);
        }
    }

    final protected <C> void edit(Class<C> editControllerClass){
        if (this.formState != FxFormState.Viewing){
            DialogUtils.error("当前窗体不在浏览状态，无法执行此操作!");
            return;
        }
        Object parent = getParent();
        if (parent instanceof FxListDataController){
            FxListDataController fxList = (FxListDataController) parent;
            fxList.edit(masterData, editControllerClass);
            this.closeForm();
        }
    }


    final protected void delete(){
        if (this.formState != FxFormState.Viewing){
            DialogUtils.error("当前窗体不在浏览状态，无法执行此操作!");
            return;
        }
        DialogUtils.confirm("准备删除此笔数据，您确认要执行此操作马?", (clickOk)->{
            if (clickOk) {
                FxManyIdParam param = new FxManyIdParam();
                param.getIds().add(masterData.getId());
                CallResult<Integer> op = getDataHandler().delete(param);
                if (op.isSuccess()) {
                    DialogUtils.error(op.getMessage());
                    return;
                }
                Object parent = getParent();
                if (parent instanceof FxListDataController){
                    FxListDataController fxList = (FxListDataController) parent;
                    fxList.loadPageRows();
                }
                closeForm();
            }
        });
    }


    public T getOldMasterData() {
        return oldMasterData;
    }

    public void setOldMasterData(T oldMasterData) {
        this.oldMasterData = oldMasterData;
    }

    public T getMasterData() {
        return masterData;
    }

    public void setMasterData(T masterData) {
        this.masterData = masterData;
    }

    public OpType getOpType() {
        return opType;
    }

    public void setOpType(OpType opType) {
        this.opType = opType;
    }

    public FxFormState getFormState() {
        return formState;
    }

    public void setFormState(FxFormState formState) {
        this.formState = formState;
    }



}
