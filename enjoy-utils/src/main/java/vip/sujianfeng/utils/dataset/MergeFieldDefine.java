package vip.sujianfeng.utils.dataset;


import vip.sujianfeng.utils.dataset.enums.DatasetFieldMergeType;

import java.util.List;

public class MergeFieldDefine {
    /**
     * 来源数据集主键列表
     */
    private List<String> srcKeys;
    /**
     * 取值来源字段
     */
    private String srcField;
    /**
     * 目标数据集主键列表
     */
    private List<String> destKeys;
    /**
     * 赋值目标字段
     */
    private String destField;
    /**
     * 数据合并方式
     */
    private DatasetFieldMergeType mergeType;

    public MergeFieldDefine(String srcField, String destField, DatasetFieldMergeType mergeType) {
        this.srcField = srcField;
        this.destField = destField;
        this.mergeType = mergeType;
    }

    public List<String> getSrcKeys() {
        return srcKeys;
    }

    public void setSrcKeys(List<String> srcKeys) {
        this.srcKeys = srcKeys;
    }

    public String getSrcField() {
        return srcField;
    }

    public void setSrcField(String srcField) {
        this.srcField = srcField;
    }

    public List<String> getDestKeys() {
        return destKeys;
    }

    public void setDestKeys(List<String> destKeys) {
        this.destKeys = destKeys;
    }

    public String getDestField() {
        return destField;
    }

    public void setDestField(String destField) {
        this.destField = destField;
    }

    public DatasetFieldMergeType getMergeType() {
        return mergeType;
    }

    public void setMergeType(DatasetFieldMergeType mergeType) {
        this.mergeType = mergeType;
    }
}
