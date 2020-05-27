package com.k.es.old;

import com.hanweb.common.util.StringUtil;
import com.hanweb.searchcore.client.query.DeleteQuery;
import com.hanweb.searchcore.client.query.QueryParams;
import com.hanweb.searchcore.client.query.UpdateQuery;
import com.hanweb.sjby.entity.Comment;
import com.hanweb.searchcore.client.query.InsertQuery;
import com.hanweb.searchcore.client.write.Writer;

/**
 * @Description: 评价数据写入ES
 * @Author gaozc
 * @Date 2019/8/7
 */
public class CommentIndexWriter {
    /**
     * 普通新增
     */
    public static boolean add(Comment comment) {
        if (comment == null) {
            return false;
        }
        Writer writer = Writer.getInstance();
        // 此处需要优先设置主键值，insertquery的第二个参数
        InsertQuery insertQuery = new InsertQuery("comment", comment.getEvaluateId());
        insertQuery.addString("task_code", comment.getTaskCode());
        insertQuery.addString("task_handle_item", comment.getTaskHandleItem());
        insertQuery.addString("task_name", comment.getTaskName());
        insertQuery.addString("sub_matter", comment.getSubMatter());
        insertQuery.addString("project_no", comment.getProjectNo());
        insertQuery.addString("pro_depart", comment.getProDepart());
        insertQuery.addString("cert_key", comment.getCertKey());
        insertQuery.addString("cert_key_gov", comment.getCertKeyGov());
        insertQuery.addString("eval_detail", comment.getEvalDetail());
        insertQuery.addString("writing_evaluation", comment.getWritingEvaluation());
        insertQuery.addDateTime("assess_time", comment.getAssessTime());
        insertQuery.addString("rectification", comment.getRectification());
        insertQuery.addString("flag", comment.getFlag());
        insertQuery.addString("data_source", comment.getDataSource());
        boolean success = writer.add(insertQuery);
        return success;
    }

    /**
     * 根据条件更新，类似 update table set field=xx where field=xxx
     *
     * @param comment 评价更新实体
     */
    public static void modify(Comment comment) {
        if (comment == null) {
            return;
        }
        String appStatus = comment.getAppStatus();
        String assessNumber = comment.getAssessNumber();
        String effectivEvalua = comment.getEffectivEvalua();
        String projectNo = comment.getProjectNo();
        String writingEvaluation = comment.getWritingEvaluation();
        String rectification = comment.getRectification();
        String flag = comment.getFlag();
        Writer writer = Writer.getInstance();
        QueryParams params = new QueryParams(" _id:${id}");
        params.addString("id", comment.getEvaluateId());
        UpdateQuery updateQuery = new UpdateQuery("comment", params);
        updateQuery.addString("appStatus", appStatus);
        updateQuery.addString("effectivEvalua", effectivEvalua);
        //不为空的时候更新，因为原来规定的更新不可以修改文字，如果原来有内容会被覆盖，所以加个判断
        if (StringUtil.isNotEmpty(writingEvaluation)) {
            System.out.println(writingEvaluation);
            updateQuery.addString("writing_evaluation", writingEvaluation);
        }
        updateQuery.addString("rectification", rectification);
        updateQuery.addString("flag", flag);
        writer.modify(updateQuery);
    }

    /**
     * 普通新增
     */
    public static boolean delete(Comment comment) {
        if (comment == null) {
            return false;
        }
        Writer writer = Writer.getInstance();
        // 此处需要优先设置主键值，insertquery的第二个参数
        DeleteQuery deleteQuery = new DeleteQuery("comment", comment.getEvaluateId());
        return writer.remove(deleteQuery);
    }
}
