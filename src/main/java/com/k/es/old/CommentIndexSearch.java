package com.k.es.old;

import com.hanweb.sjby.entity.Comment;

import java.util.Date;

/**
 * 搜索评价数据
 *
 * @author gaozc
 * @date 2019.08.10
 */
public class CommentIndexSearch {

    /**
     * @Description:关键词统计，不传参数这是全部的数据总量
     * @author zhangmax
     * @date 2019/8/12 17:41
     *
     * @param pageTitle 评价内容
     * @param date 统计截止时间
     * @return long 条数
     *
     */
    public static boolean exists(String iid) {
        SearchQuery searchQuery = new SearchQuery();
        searchQuery.setFrom("comment");
        QueryParams queryParams = new QueryParams(" evaluate_id:${evaluate_id}");
        queryParams.addString("evaluate_id", iid);
        searchQuery.setWhere(queryParams);
        return (int) Searcher.getInstance().searchCount(searchQuery) > 0;
    }



}
