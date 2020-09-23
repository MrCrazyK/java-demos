package com.k.es.util;

import lombok.Data;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.sort.SortOrder;

/**
 * Es查询实体的工具类
 * 
 * @author lijingtao
 * @date 2020年1月3日
 */
@Data
public class EsQueryVo {

	/**
	 * 索引的名称
	 */
	private String indices;

	/**
	 * 索引的类型
	 */
	private String types;

	/**
	 * 需要查询的字段
	 */
	private String[] filds;

	/**
	 * 从第几个开始查询(默认为0)
	 */
	private int from = 0;

	/**
	 * 查询的页码
	 */
	private int pageNo;

	/**
	 * 查询的条数（默认为10）
	 */
	private int size = 10;

	/**
	 * 排序的字段（按照某个字段排序的话，hit.getScore()将会失效）
	 */
	private String sort;

	/**
	 * 排序的顺序，默认正序
	 */
	private SortOrder sortOrder = SortOrder.ASC;

	/**
	 * 主键
	 */
	private String pkValue;

	/**
	 * 查询条件
	 */
	private BoolQueryBuilder boolQuery;

	/**
	 * Script
	 */
	private Script script;

}
