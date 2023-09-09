package vip.sujianfeng.utils;

import vip.sujianfeng.utils.comm.MapUtilsEx;
import vip.sujianfeng.utils.dataset.DatasetMerger;
import vip.sujianfeng.utils.dataset.DatasetRows;
import vip.sujianfeng.utils.dataset.MergeFieldDefine;
import vip.sujianfeng.utils.dataset.enums.DatasetFieldMergeType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * author SuJianFeng
 * createTime  2023/7/11
 * @Description
 **/
public class DatasetMergerTest {

    public static void main(String[] args) {

        List<Map<String, Object>> mainRows = new ArrayList<>();
        mainRows.add(MapUtilsEx.mapOf("personId", "p002", "totalScore", null, "avgScore", null, "maxScore", null, "minScore", null));
        mainRows.add(MapUtilsEx.mapOf("personId", "p001", "totalScore", null, "avgScore", null, "maxScore", null, "minScore", null));
        mainRows.add(MapUtilsEx.mapOf("personId", "p001", "totalScore", null, "avgScore", null, "maxScore", null, "minScore", null));
        mainRows.add(MapUtilsEx.mapOf("personId", "p001", "totalScore", null, "avgScore", null, "maxScore", null, "minScore", null));
        mainRows.add(MapUtilsEx.mapOf("personId", "p002", "totalScore", null, "avgScore", null, "maxScore", null, "minScore", null));
        mainRows.add(MapUtilsEx.mapOf("personId", "p002", "totalScore", null, "avgScore", null, "maxScore", null, "minScore", null));

        List<Map<String, Object>> subRows = new ArrayList<>();
        subRows.add(MapUtilsEx.mapOf("personId", "p002", "score", 50));
        subRows.add(MapUtilsEx.mapOf("personId", "p002", "score", 30));
        subRows.add(MapUtilsEx.mapOf("personId", "p001", "score", 10));
        subRows.add(MapUtilsEx.mapOf("personId", "p001", "score", 20));

        DatasetRows<Map<String, Object>> mainDataset = new DatasetRows<>(mainRows, Collections.singletonList("personId"));
        DatasetRows<Map<String, Object>> subDataset = new DatasetRows<>(subRows, Collections.singletonList("personId"));
        List<MergeFieldDefine> mergeFieldDefines = new ArrayList<>();
        mergeFieldDefines.add(new MergeFieldDefine( "score", "totalScore", DatasetFieldMergeType.Sum));
        mergeFieldDefines.add(new MergeFieldDefine( "score", "avgScore", DatasetFieldMergeType.Avg));
        mergeFieldDefines.add(new MergeFieldDefine( "score", "maxScore", DatasetFieldMergeType.Max));
        mergeFieldDefines.add(new MergeFieldDefine( "score", "minScore", DatasetFieldMergeType.Min));

        List<Map<String, Object>> result = new DatasetMerger<>(mainDataset, subDataset, mergeFieldDefines).merge((mainRow, subRow) -> {
            System.out.println(mainRow);
        });
        System.out.println(result);
    }
}
