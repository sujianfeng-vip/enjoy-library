package vip.sujianfeng.utils.dataset;


import vip.sujianfeng.utils.dataset.enums.DatasetFieldMergeType;

import java.util.List;

public class MergeFieldDefine {
    /**
     * Source Dataset Primary Key List
     */
    private List<String> srcKeys;
    /**
     * Value Source Field
     */
    private String srcField;
    /**
     * Target Dataset Primary Key List
     */
    private List<String> destKeys;
    /**
     * Assign Target Fields
     */
    private String destField;
    /**
     * Data Merge Method
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
