package com.k.es.old;

import com.hanweb.searchcore.client.query.InsertQuery;
import com.hanweb.searchcore.client.write.Writer;
import com.hanweb.sjby.entity.AnalysisEvaluation;

/**
 * @Description: 评价数据写入ES
 * @Author gaozc
 * @Date 2019/8/7
 */
public class AnalysisIndexWriter {
    /**
     * 普通新增
     * @param tableName 索引表名
     * @param analysisEvaluation 统计数据
     * @return 成功与否
     */
    public static boolean add(String tableName, AnalysisEvaluation analysisEvaluation) {
        if (analysisEvaluation == null) {
            return false;
        }
        Writer writer = Writer.getInstance();
        // 此处需要优先设置主键值，insertquery的第二个参数
        InsertQuery insertQuery = new InsertQuery(tableName, analysisEvaluation.getIid() + "");
        insertQuery.addString("task_code", analysisEvaluation.getTaskCode());
        insertQuery.addString("count", analysisEvaluation.getCount());
        insertQuery.addString("version_id", analysisEvaluation.getVersionId() + "");
        insertQuery.addString("sharding_id", analysisEvaluation.getShardingId() + "");
        return writer.add(insertQuery);
    }
}
