package vip.sujianfeng.utils.dataset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.sujianfeng.utils.comm.StringBuilderEx;
import vip.sujianfeng.utils.dataset.enums.DatasetFieldMergeType;
import vip.sujianfeng.utils.dataset.intf.MergerIntercept;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dataset Merge Tool
 */
public class DatasetMerger<A, B> {
    private static Logger logger = LoggerFactory.getLogger(DatasetMerger.class);

    private DatasetRows<A> mainDataset;
    private DatasetRows<B> subDataset;
    private List<MergeFieldDefine> mergeFieldDefines;

    private Map<String, Map<String, List<B>>> map = new HashMap<>();

    public DatasetMerger(DatasetRows<A> mainDataset, DatasetRows<B> subDataset, List<MergeFieldDefine> mergeFieldDefines) {
        this.mainDataset = mainDataset;
        this.subDataset = subDataset;
        this.mergeFieldDefines = mergeFieldDefines;
    }

    public List<A> merge(MergerIntercept<A,B> mergerIntercept) {
        buildMap(this.mainDataset.getSortFields(), this.subDataset.getSortFields());
        for (MergeFieldDefine mergeDefine : this.mergeFieldDefines) {
            List<String> mainKeys = mergeDefine.getDestKeys() != null && mergeDefine.getDestKeys().size() > 0 ? mergeDefine.getDestKeys() : this.mainDataset.getSortFields();
            List<String> subKeys = mergeDefine.getSrcKeys() != null && mergeDefine.getSrcKeys().size() > 0 ? mergeDefine.getSrcKeys() : this.subDataset.getSortFields();
            buildMap(mainKeys, subKeys);
            mergeByDefine(mergeDefine, mainKeys, subKeys);
        }
        mergeByIntercept(mergerIntercept, this.mainDataset.getSortFields(), this.subDataset.getSortFields());
        return this.mainDataset.getData();
    }

    public void buildMap(List<String> mainKeys, List<String> subKeys) {
        if (mainKeys.size() == 0 || subKeys.size() == 0) {
            return;
        }
        StringBuilderEx mapKey = new StringBuilderEx();
        mainKeys.forEach(it-> mapKey.append(it).append(","));
        subKeys.forEach(it-> mapKey.append(it).append(","));
        if (map.containsKey(mapKey.toString())) {
            return;
        }
        Map<String, List<B>> subMap = new HashMap<>();
        map.put(mapKey.toString(), subMap);
        this.mainDataset.getData().sort((o1, o2) -> {
            String v1 = this.mainDataset.getKeyValues(o1, mainKeys);
            String v2 = this.mainDataset.getKeyValues(o2, mainKeys);
            return v1.compareTo(v2);
        });
        this.subDataset.getData().sort((o1, o2) -> {
            String v1 = this.subDataset.getKeyValues(o1, subKeys);
            String v2 = this.subDataset.getKeyValues(o2, subKeys);
            return v1.compareTo(v2);
        });
        int subIndex = 0;
        //Both the main dataset and the subset are sorted and then iterated once, which is the highest efficiency
        for (A mainRow : this.mainDataset.getData()) {
            String mainKeyValues = this.mainDataset.getKeyValues(mainRow, mainKeys);
            if (subMap.containsKey(mainKeyValues)) {
                continue;
            }
            List<B> subList = new ArrayList<>();
            subMap.put(mainKeyValues, subList);
            if (subIndex <= this.subDataset.getData().size()) {
                while (subIndex < this.subDataset.getData().size()) {
                    B subRow = this.subDataset.getData().get(subIndex);
                    String subKeyValues = this.subDataset.getKeyValues(subRow, subKeys);
                    int compareValue = mainKeyValues.compareTo(subKeyValues);
                    if (compareValue < 0) {
                        //If the main data row is less than the sub data row, exit the sub dataset loop
                        break;
                    }
                    if (compareValue > 0) {
                        //If the main data row>sub data row, then the sub data row is moved down
                        subIndex ++;
                        continue;
                    }
                    subList.add(subRow);
                    subIndex ++;
                }
            }
        }
    }

    public void mergeByDefine(MergeFieldDefine mergeDefine, List<String> mainKeys, List<String> subKeys) {
        StringBuilderEx mapKey = new StringBuilderEx();
        mainKeys.forEach(it-> mapKey.append(it).append(","));
        subKeys.forEach(it-> mapKey.append(it).append(","));
        for (A mainRow : this.mainDataset.getData()) {
            String mainKeyValues = this.mainDataset.getKeyValues(mainRow, mainKeys);
            Map<String, List<B>> subMap = this.map.get(mapKey.toString());
            if (subMap == null) {
                //No matching sub dataset exists
                continue;
            }
            List<B> subRows = subMap.get(mainKeyValues);
            if (subRows != null) {
                subRows.forEach(subRow -> {
                    Object srcValue = this.subDataset.getFieldValue(subRow, mergeDefine.getSrcField());
                    switch (mergeDefine.getMergeType()) {
                        case Sync:
                            this.mainDataset.setFieldValue(mainRow, mergeDefine.getDestField(), srcValue);
                            break;
                        case Sum:
                        case Avg:
                            this.mainDataset.addFieldValue(mainRow, mergeDefine.getDestField(), srcValue);
                            break;
                        case Max:
                            this.mainDataset.maxFieldValue(mainRow, mergeDefine.getDestField(), srcValue);
                            break;
                        case Min:
                            this.mainDataset.minFieldValue(mainRow, mergeDefine.getDestField(), srcValue);
                            break;
                    }
                });
                if (subRows.size() > 0) {
                    if (mergeDefine.getMergeType() == DatasetFieldMergeType.Avg) {
                        //Calculate Average
                        this.mainDataset.avgFieldValue(mainRow, mergeDefine.getDestField(), subRows.size());
                    }
                }
            }
        }
    }

    public void mergeByIntercept(MergerIntercept<A,B> mergerIntercept, List<String> mainKeys, List<String> subKeys) {
        StringBuilderEx mapKey = new StringBuilderEx();
        mainKeys.forEach(it-> mapKey.append(it).append(","));
        subKeys.forEach(it-> mapKey.append(it).append(","));
        for (A mainRow : this.mainDataset.getData()) {
            String mainKeyValues = this.mainDataset.getKeyValues(mainRow, mainKeys);
            Map<String, List<B>> subMap = this.map.get(mapKey.toString());
            if (subMap != null) {
                List<B> subRows = subMap.get(mainKeyValues);
                if (subRows != null && subRows.size() > 0) {
                    subRows.forEach(subRow -> {
                        mergerIntercept.rowMerge(mainRow, subRow);
                    });
                    continue;
                }
            }
            //Trigger interception event even if there is no sub dataset
            mergerIntercept.rowMerge(mainRow, null);
        }
    }

}
