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
public class CreateAnalysisTable extends BaseTask {

	/**
	 * 建表（索引库）
	 * @param tableName 表名
	 */
	public void createCommentTable(String tableName) {
		IndexTableCmd indexTableCmd = IndexCmdFactory.getIndexTableCmd();
		IndexTable indexTable = new IndexTable(tableName, 2, 1);
		indexTable.addColumn(new IndexColumn("iid", "主键", IndexColumnType.INT));
		indexTable.addColumn(new IndexColumn("task_code", "事项编码", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("count", "json正文数据", IndexColumnType.STRING));
		indexTable.addColumn(new IndexColumn("version_id", "版本 norm_version表中的主键id", IndexColumnType.INT));
		indexTable.addColumn(new IndexColumn("sharding_id", "表区分码", IndexColumnType.INT));
		CmdResponse cmdResponse = indexTableCmd.add(indexTable);
		System.out.println(JsonUtil.objectToString(cmdResponse));
		// 创建附表--给下面的测试用
		// createAssistantTable();
	}

	@Override protected void config() {
		setTaskId("create_analysis_tables");
		setTaskName("创建数据分析的三十几张表的es索引");
		setTaskSchedule(TaskScheduleBuilder.getOnceSchedule());
	}

	@Override protected void doWork(JobDataMap jobDataMap) {
		//所有需要同步的表名
		String[] tables = new String[] {"BuWei", "BeiJin", "Tianjin", "HeBei", "ShanXiSheng",
				"NeiMengGuZiZhiQu", "LiaoNing", "JiLin", "HeiLongJiang", "ShangHai", "JiangSu",
				"ZheJiang", "AnHui", "FuJian", "JiangXi", "ShanDong", "HeNan", "HuBei", "HuNan",
				"GuangDong", "GuangXiZhuangZuZiZhiQu", "HaiNan", "ChongQing", "SiChuan", "GuiZhou",
				"YunNan", "XiZangZiZhiQu", "ShanXi", "GanSu", "QingHai", "NingXiaHuiZuZiZhiQu",
				"XinJiangWeiWuErZiZhiQu", "XinJiangShengChanJianSheBingTuan"};
		try {
			for (int i = 0; i < tables.length; i++) {
				createCommentTable("analysisevaluation_" + tables[i]);
			}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
