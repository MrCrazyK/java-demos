package com.k.es.old;

import com.hanweb.common.task.BaseTask;
import com.hanweb.common.task.TaskScheduleBuilder;
import com.hanweb.common.util.JsonUtil;
import com.hanweb.searchcore.client.command.CmdResponse;
import com.hanweb.searchcore.client.command.IndexCmdFactory;
import com.hanweb.searchcore.client.command.IndexTableCmd;
import com.hanweb.searchcore.entity.IndexColumn;
import com.hanweb.searchcore.entity.IndexColumnType;
import com.hanweb.searchcore.entity.IndexTable;
import org.quartz.JobDataMap;

/**
 * @Description: es创建索引库类
 * @Author gaozc
 * @Date 2019/8/6
 */
public class CreateCommentTable extends BaseTask {

	/**
	 * 建办件评价表（索引库）
	 */
	public void createCommentTable() {
		IndexTableCmd indexTableCmd = IndexCmdFactory.getIndexTableCmd();
		IndexTable indexTable = new IndexTable("comment", 2, 1);
		indexTable.addColumn(new IndexColumn("evaluateId", "评价唯一标示", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("sourceName", "来源名称", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("sourceCode", "来源编码", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("deptCode", "统一社会信用代码", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("taskCode", "事项编码", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("taskHandleItem", "业务编码", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("taskName", "事项名称", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("subMatter", "事项主题", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("projectNo", "办件编号", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("proStatus", "办理状态", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("proDepart", "受理部门", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("pf", "评价渠道", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("certKey", "线上散列值", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("certKeyGov", "线下散列值散列值", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("userName", "用户姓名", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("userProp", "用户属性", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("satisfaction", "整体满意度", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("evalDetail", "评价详情", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("writingEvaluation", "文字评价", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("assessTime", "评价时间", IndexColumnType.DATETIME));
		indexTable.addColumn(new IndexColumn("assessNumber", "评价次数", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("appStatus", "审核状态", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("rectification", "整改反馈", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("flag", "操作类型", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("dataSource", "数据来源", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("dataSort", "数据分类", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("promisetime", "是否承诺件", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("effectivEvalua", "是否为有效评价", IndexColumnType.STRING));
		//即地方或部委审核后调用修改接口的时间
		indexTable.addColumn(new IndexColumn("updateTime", "修改时间", IndexColumnType.DATETIME));
		indexTable.addColumn(new IndexColumn("addTime", "创建时间", IndexColumnType.DATETIME));
		indexTable.addColumn(new IndexColumn("acceptTime", "收件时间", IndexColumnType.DATETIME));
		indexTable.addColumn(new IndexColumn("applyDate", "申报时间", IndexColumnType.DATETIME));
		indexTable.addColumn(new IndexColumn("resultDate", "办结时间", IndexColumnType.DATETIME));
		indexTable.addColumn(new IndexColumn("promiseDate", "承诺办结时间", IndexColumnType.DATETIME));
		indexTable.addColumn(new IndexColumn("limitSceneNum", "到办事现场次数", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("isFree", "是否收费", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("inSyncEs", "内网是否同步到es", IndexColumnType.INT));
		indexTable.addColumn(new IndexColumn("outSyncEs", "互联网区是否同步es", IndexColumnType.INT));
		indexTable.addColumn(new IndexColumn("alternate1", "备用字段1", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("alternate2", "备用字段2", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("alternate3", "备用字段3", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("alternate4", "备用字段4", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("alternate5", "备用字段5", IndexColumnType.STRING));
		CmdResponse cmdResponse = indexTableCmd.add(indexTable);
		//即地方或部委审核后调用修改接口的时间
		System.out.println(JsonUtil.objectToString(cmdResponse));
		// 创建附表--给下面的测试用
	}

	@Override protected void config() {
		setTaskId("create_table_index");
		setTaskName("MQ中评价数据存数据库和ES");
		setTaskSchedule(TaskScheduleBuilder.getOnceSchedule());
	}

	@Override protected void doWork(JobDataMap jobDataMap) {
        System.out.println("开始咯哦");
		try {
            createCommentTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("结束咯哦");
    }
}
