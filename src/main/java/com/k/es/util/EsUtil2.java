package com.k.es.util;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.search.SearchAction;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.InnerHitBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.join.query.HasChildQueryBuilder;
import org.elasticsearch.join.query.JoinQueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * 操作es的工具类
 *
 * @author lijingtao
 * @date 2019年12月26日
 */
public class EsUtil2 {

    /**
     * 连接es的客户端
     */
    @Resource
    private static RestHighLevelClient client;

    /**
     * 去除回车的特殊字符
     */
    public static String replaceAllEnter(String str) {
        String result = "";
        if (!StringUtils.isEmpty(str)) {
            result = str.replaceAll("(\r\n|\n)", "^^");
        }
        return result;
    }

    /**
     * 去除回车、\"、\\的特殊字符
     */
    public static String replaceAllEsStr(String str) {
        String result = "";
        if (!StringUtils.isEmpty(str)) {
            // 将空格回车替换成^^
            result = str.replaceAll("(\r\n|\n)", "^^");
            // 将中文的双引号以及英文的双引号替换成\'
            result = str.replaceAll("([“”\"])", "\\\\'");
            // 将连续的2个\\ 替换成三个\\\
            result = str.replaceAll("\\\\", "\\\\\\'");
        }
        return result;
    }

    /**
     * 创建data类型格式化是yyyy-MM-dd HH:mm:ss的mapping
     *
     * @param _index    索引名称
     * @param _type     索引类型
     * @param fieldName data类型的字符串数组
     */
    public static void createDateMapping(String _index, String _type, String[] fieldName) {
        PutMappingRequest mapping = Requests.putMappingRequest(_index).type(_type)
                .source(createDataJsonBuilder(fieldName));
        try {
            RequestOptions builder = RequestOptions.DEFAULT;
            client.indices().putMapping(mapping, builder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建es中data类型的jsonBuilder
     *
     * @param fieldName data类型的字符串数组
     */
    public static XContentBuilder createDataJsonBuilder(String[] fieldName) {
        XContentBuilder mapping = null;
        try {
            mapping = jsonBuilder().startObject()
                    // 设置type中的属性
                    .startObject("properties");
            for (int i = 0; i < fieldName.length; i++) {
                mapping.startObject(fieldName[i]).field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss")
                        .endObject();
            }
            mapping.endObject().endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapping;
    }

    /**
     * 创建es中join类型的jsonBuilder
     */
    @Deprecated
    @SuppressWarnings("unused")
    private XContentBuilder createJoinJsonBuilder() {
        XContentBuilder mapping = null;
        try {
            mapping = jsonBuilder().startObject()
                    // 设置type中的属性
                    .startObject("properties").startObject("joinField").field("eager_global_ordinals", "true")
                    .field("type", "join").startObject("relations").field("evaluate", "evaluateAdditional").endObject()
                    .endObject().endObject().endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapping;
    }

//    /**
//     * 向es的evaluate索引表中插入追评数据
//     */
//    @Deprecated
//    @SuppressWarnings("unused")
//    private boolean insetJoinIndex(Object obj) {
//        if (obj == null) {
//            return false;
//        }
//        String pkValue = "id";
//        InsertQuery insertQuery = new InsertQuery("evaluate", pkValue);
//        reflectToInsertQuery(obj, insertQuery);
//        IndexRequest indexRequest = new IndexRequest(insertQuery.getTableName(), insertQuery.getTableName(),
//                insertQuery.getPkValue());
//        String jsonDocument = insertQuery.getJsonDocument(insertQuery.getTableName());
//        jsonDocument = jsonDocument.substring(0, jsonDocument.length() - 1);
//        jsonDocument += ",\"joinField\":{\"name\":\"evaluateAdditional\",\"parent\":\"" + "Pid" + "\"}}";
//        indexRequest.source(jsonDocument, XContentType.JSON);
//        // 设置路由值
//        indexRequest.routing("Pid");
//        try {
//            client.index(indexRequest, RequestOptions.DEFAULT);
//            return true;
//        } catch (IOException e) {
//            return false;
//        }
//    }

    /**
     * @param vo
     * @param scriptName
     * @param script
     * @throws java.io.IOException
     */
    public static SearchResponse searchEsByScript(EsQueryVo vo, String scriptName, Script script) throws IOException {
//      demo写法，不要删除，防止不会写
//		scriptName = "dataSourceName";
//		Map<String, Object> params = new HashMap<>();
//		String inlineScript = "if(doc['taskName'].value.length() > 3) { return doc['taskName'].value.substring(1,3);} return doc['taskName'].value.substring(1,3)";
//		script = new Script(ScriptType.INLINE, "painless", inlineScript, params);

        SearchRequestBuilder searchRequestBuilder = new SearchRequestBuilder(null, SearchAction.INSTANCE);
        // 设置索引
        searchRequestBuilder.setIndices(vo.getIndices());
        // 设置type
        searchRequestBuilder.setTypes(vo.getTypes());
        // 设置查询的字段，String数组
        if (vo.getFilds() != null && vo.getFilds().length > 0) {
            searchRequestBuilder.setFetchSource(vo.getFilds(), null);
        } else {
            searchRequestBuilder.setFetchSource("*", null);
        }
        if (vo.getPageNo() > 0) {// 如果传过来页码，使用页码查询
            int from = (vo.getPageNo() - 1) * vo.getSize();
            searchRequestBuilder.setFrom(from);
        } else {// 如果没有传明确的页码，使用from查询
            searchRequestBuilder.setFrom(vo.getFrom());
        }
        // 设置查询条数
        searchRequestBuilder.setSize(vo.getSize());
        // 设置script查询
        if (script != null) {
            searchRequestBuilder.addScriptField(scriptName, script);
        }

        String cityCode = "110000";

        String quaryinfo = "doc['deptCode'].value.substring(2,8):" + cityCode;
        QueryStringQueryBuilder qs = QueryBuilders.queryStringQuery(quaryinfo);
        searchRequestBuilder.setQuery(qs);

        // 查询
        return client.search((SearchRequest) searchRequestBuilder.request(), RequestOptions.DEFAULT);
//      demo写法，不要删除，防止不会写（获取结果）
//		SearchHit[] hits = search.getHits().getHits();
//		System.out.println(hits);
    }

    /**
     * 根据正则表达式查询es
     */
    public static SearchResponse searchEsByRegexp(EsQueryVo vo, BoolQueryBuilder should) throws IOException {
        return searchEsByRegexp(vo, should, null);
    }

    /**
     * @param aggregationBuilder 分组
     */
    public static SearchResponse searchEsByRegexp(EsQueryVo vo, BoolQueryBuilder should,
                                                  TermsAggregationBuilder aggregationBuilder) throws IOException {
        return searchEsByRegexp(vo, should, aggregationBuilder, true);
    }

    /**
     * @param aggregationBuilder 分组条件
     * @param fildFlag           false：表示不查询任何字段，提高速度
     */
    public static SearchResponse searchEsByRegexp(EsQueryVo vo, BoolQueryBuilder should,
                                                  TermsAggregationBuilder aggregationBuilder, Boolean fildFlag) throws IOException {

        SearchRequestBuilder searchRequestBuilder = new SearchRequestBuilder(null, SearchAction.INSTANCE);
        // 设置索引
        searchRequestBuilder.setIndices(vo.getIndices());
        // 设置type
        searchRequestBuilder.setTypes(vo.getTypes());

        // 设置查询的字段，String数组
        if (fildFlag) {
            if (vo.getFilds() != null && vo.getFilds().length > 0) {
                searchRequestBuilder.setFetchSource(vo.getFilds(), null);
            } else {
                searchRequestBuilder.setFetchSource("*", null);
            }
        } else {// 不查询任何字段
            searchRequestBuilder.setFetchSource(false);
        }

        if (vo.getPageNo() > 0) {// 如果传过来页码，使用页码查询
            int from = (vo.getPageNo() - 1) * vo.getSize();
            searchRequestBuilder.setFrom(from);
        } else {// 如果没有传明确的页码，使用from查询
            searchRequestBuilder.setFrom(vo.getFrom());
        }
        // 设置查询条数
        searchRequestBuilder.setSize(vo.getSize());

        // 设置排序
        if (!StringUtils.isEmpty(vo.getSort())) {
            searchRequestBuilder.addSort(vo.getSort(), vo.getSortOrder());
        }

        // demo写法，不要删除，防止不会写（正则表达式写法）
        // should =
        // QueryBuilders.boolQuery().should(QueryBuilders.regexpQuery("deptCode",
        // ".++110000.*"));
        if (should != null) {
            searchRequestBuilder.setQuery(should);
        }

        if (aggregationBuilder != null) {
            searchRequestBuilder.addAggregation(aggregationBuilder);
        }

        // 查询
        return client.search((SearchRequest) searchRequestBuilder.request(), RequestOptions.DEFAULT);

        // 获取分组之后的数据 demo
//		Aggregations aggregations = client.search((SearchRequest) searchRequestBuilder.request(),
//				RequestOptions.DEFAULT).getAggregations();
//		Aggregation aggregation = aggregations.asList().get(0);
//		List<? extends Terms.Bucket> oneBuckets = ((ParsedStringTerms) aggregation).getBuckets();
//		for (Terms.Bucket oneBucket : oneBuckets) {
//			String pushUrlKey = oneBucket.getKey().toString();
//			long docCount = oneBucket.getDocCount();
//		}
    }

    /**
     * 匹配查询es
     *
     * @param vo
     * @param should
     * @return
     * @throws java.io.IOException
     */
    public static SearchResponse searchEsByRegexp(EsQueryVo vo, QueryBuilder should) throws IOException {
        SearchRequestBuilder searchRequestBuilder = new SearchRequestBuilder(null, SearchAction.INSTANCE);
        // 设置索引
        searchRequestBuilder.setIndices(vo.getIndices());
        // 设置type
        searchRequestBuilder.setTypes(vo.getTypes());
        // 设置查询的字段，String数组
        if (vo.getFilds() != null && vo.getFilds().length > 0) {
            searchRequestBuilder.setFetchSource(vo.getFilds(), null);
        } else {
            searchRequestBuilder.setFetchSource("*", null);
        }
        if (vo.getPageNo() > 0) {// 如果传过来页码，使用页码查询
            int from = (vo.getPageNo() - 1) * vo.getSize();
            searchRequestBuilder.setFrom(from);
        } else {// 如果没有传明确的页码，使用from查询
            searchRequestBuilder.setFrom(vo.getFrom());
        }
        // 设置查询条数
        searchRequestBuilder.setSize(vo.getSize());

        // 搜索全部文档
//		QueryBuilder queryBuilde = QueryBuilders.matchAllQuery();
        // 单个匹配，搜索name为jack的文档
//		QueryBuilder queryBuilder = QueryBuilders.matchQuery("name", "jack");
        // 搜索name中或interest中包含有music的文档（必须与music一致）
//		QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery("music", "name", "interest");

        // 组合查询{"must(QueryBuilders) : AND","mustNot(QueryBuilders): NOT","should: OR"}
        // should下面会带一个以上的条件，至少满足一个条件，这个文档就符合should
//		QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("user", "kimchy"))
//				.mustNot(QueryBuilders.termQuery("message", "nihao")).should(QueryBuilders.termQuery("gender", "male"));

        // 只查询一个id的
//		QueryBuilder queryBuilder = QueryBuilders.idsQuery().ids("1");

        if (should != null) {
            searchRequestBuilder.setQuery(should);
        }

        // 查询
        return client.search((SearchRequest) searchRequestBuilder.request(), RequestOptions.DEFAULT);
    }

    /**
     * @param vo
     * @param boolQueryBuilder
     * @param aggregationBuilder
     * @param fildFlag
     * @return SearchResponse
     * @throws java.io.IOException
     * @Description:根据查询条件和聚合条件查询方法
     */
    public static SearchResponse searchEsByAggs(EsQueryVo vo, BoolQueryBuilder boolQueryBuilder,
                                                AggregationBuilder aggregationBuilder, Boolean fildFlag) throws IOException {
        SearchRequestBuilder searchRequestBuilder = new SearchRequestBuilder(null, SearchAction.INSTANCE);
        searchRequestBuilder.setIndices(vo.getIndices());
        searchRequestBuilder.setTypes(vo.getTypes());

        // 设置查询的字段，String数组
        if (fildFlag) {
            if (vo.getFilds() != null && vo.getFilds().length > 0) {
                searchRequestBuilder.setFetchSource(vo.getFilds(), null);
            } else {
                searchRequestBuilder.setFetchSource("*", null);
            }
        } else {// 不查询任何字段
            searchRequestBuilder.setFetchSource(false);
        }

        if (vo.getPageNo() > 0) {// 如果传过来页码，使用页码查询
            int from = (vo.getPageNo() - 1) * vo.getSize();
            searchRequestBuilder.setFrom(from);
        } else {// 如果没有传明确的页码，使用from查询
            searchRequestBuilder.setFrom(vo.getFrom());
        }
        // 设置查询条数
        searchRequestBuilder.setSize(vo.getSize());

        if (null != boolQueryBuilder) {
            searchRequestBuilder.setQuery(boolQueryBuilder);
        }
        if (null != aggregationBuilder) {
            searchRequestBuilder.addAggregation(aggregationBuilder);
        }
        // 查询
        return client.search((SearchRequest) searchRequestBuilder.request(), RequestOptions.DEFAULT);
    }

    /**
     * 根据HasChildQueryBuilder查询
     *
     * @return
     * @throws java.io.IOException
     */
    public static SearchResponse searchEsByHasChildQueryBuilder() throws IOException {

        SearchRequestBuilder searchRequestBuilder = new SearchRequestBuilder(null, SearchAction.INSTANCE);
        // 设置索引
        searchRequestBuilder.setIndices("my_join_index");
        // 设置type
        searchRequestBuilder.setTypes("_doc");
        // 设置查询的字段，String数组
        searchRequestBuilder.setFetchSource("*", null);
        searchRequestBuilder.setFrom(0);
        // 设置查询条数
        searchRequestBuilder.setSize(10);

        HasChildQueryBuilder hasSecondChildQueryBuilder = new HasChildQueryBuilder("answer",
                QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("text", "This is a question")),
                ScoreMode.Max);

        searchRequestBuilder.setQuery(hasSecondChildQueryBuilder.query());

        // 查询
        return client.search((SearchRequest) searchRequestBuilder.request(), RequestOptions.DEFAULT);
    }

    public static SearchResponse searchEsByJoinAgg2() throws IOException {

        SearchRequestBuilder searchRequestBuilder = new SearchRequestBuilder(null, SearchAction.INSTANCE);
        // 设置索引
        searchRequestBuilder.setIndices("three_tree_index");
        // 设置type
        searchRequestBuilder.setTypes("_doc");
        // 设置查询的字段，String数组
        searchRequestBuilder.setFetchSource("*", null);
        searchRequestBuilder.setFrom(0);
        // 设置查询条数
        searchRequestBuilder.setSize(10);

        QueryBuilder qb = JoinQueryBuilders
                .hasParentQuery("article",
                        QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("article_desc", "xiaoming")), true)
                .innerHit(new InnerHitBuilder());
        searchRequestBuilder.setQuery(qb);

        // 查询
        return client.search((SearchRequest) searchRequestBuilder.request(), RequestOptions.DEFAULT);

        // 对查询结果的操作demo不要删除，防止不会写
//		SearchResponse search = client.search((SearchRequest) searchRequestBuilder.request(),
//				RequestOptions.DEFAULT);
//		SearchHit[] hits = search.getHits().getHits();
//		for (int i = 0; i < hits.length; i++) {
//			Map<String, Object> sourceAsMap = hits[i].getSourceAsMap();
//			System.out.println(sourceAsMap);
//			Map<String, SearchHits> innerHits = hits[i].getInnerHits();
//			System.out.println(innerHits);
//			// 查询父级结构
//			SearchHits searchHits = innerHits.get("article");
//			SearchHit[] hits2 = searchHits.getHits();
//		}
    }

    /**
     * 根据父级的条件查询子级的结构，查询结构附带父级的结构内容
     *
     * @return
     * @throws java.io.IOException
     */
    public static SearchResponse searchChildEsByParentJoin() throws IOException {

        SearchRequestBuilder searchRequestBuilder = new SearchRequestBuilder(null, SearchAction.INSTANCE);
        // 设置索引
        searchRequestBuilder.setIndices("bjcomment");
        // 设置type
        searchRequestBuilder.setTypes("bjcomment");
        // 设置查询的字段，String数组
        searchRequestBuilder.setFetchSource("*", null);
        searchRequestBuilder.setFrom(0);
        // 设置查询条数
        searchRequestBuilder.setSize(10);
        // 排序
        searchRequestBuilder.addSort("serialNumber", SortOrder.ASC);

        // 根据父级的条件查询子级的结构；innerHit表示查询结果附带父级的结构
        QueryBuilder qb = JoinQueryBuilders
                .hasParentQuery("firstComment",
                        QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("userName", "李敬涛")), true)
                .innerHit(new InnerHitBuilder());
        searchRequestBuilder.setQuery(qb);

        // 查询
        return client.search((SearchRequest) searchRequestBuilder.request(), RequestOptions.DEFAULT);
    }

    /**
     * 根据父级的条件查询子级的结构，查询结构附带父级的结构内容
     *
     * @return
     * @throws java.io.IOException
     */
    public static SearchResponse searchChildEsByParentJoin2() throws IOException {

        SearchRequestBuilder searchRequestBuilder = new SearchRequestBuilder(null, SearchAction.INSTANCE);
        // 设置索引
        searchRequestBuilder.setIndices("evaluate");
        // 设置type
        searchRequestBuilder.setTypes("evaluate");
        // 设置查询的字段，String数组
        searchRequestBuilder.setFetchSource("*", null);
        searchRequestBuilder.setFrom(0);
        // 设置查询条数
        searchRequestBuilder.setSize(10);

        // 根据父级的条件查询子级的结构；innerHit表示查询结果附带父级的结构
        QueryBuilder qb = JoinQueryBuilders
                .hasParentQuery("evaluate",
                        QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("regionName", "北京市")), true)
                .innerHit(new InnerHitBuilder());
        searchRequestBuilder.setQuery(qb);

        // 查询
        return client.search((SearchRequest) searchRequestBuilder.request(), RequestOptions.DEFAULT);
    }

    /**
     * 只查询父级的文档数据
     *
     * @return
     * @throws java.io.IOException
     */
    public static SearchResponse searchOnlyParentJoin() throws IOException {
        SearchRequestBuilder searchRequestBuilder = new SearchRequestBuilder(null, SearchAction.INSTANCE);
        // 设置索引
        searchRequestBuilder.setIndices("evaluate");
        // 设置type
        searchRequestBuilder.setTypes("evaluate");
        // 设置查询的字段，String数组
        searchRequestBuilder.setFetchSource("*", null);
        searchRequestBuilder.setFrom(0);
        // 设置查询条数
        searchRequestBuilder.setSize(10);

        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        qb.must(QueryBuilders.termQuery("joinField", "evaluate"));
        searchRequestBuilder.setQuery(qb);

        // 查询
        return client.search((SearchRequest) searchRequestBuilder.request(), RequestOptions.DEFAULT);
    }

    /**
     * 只查询自己的文档数据
     *
     * @return
     * @throws java.io.IOException
     */
    public static SearchResponse searchOnlyChildJoin() throws IOException {
        SearchRequestBuilder searchRequestBuilder = new SearchRequestBuilder(null, SearchAction.INSTANCE);
        // 设置索引
        searchRequestBuilder.setIndices("evaluate");
        // 设置type
        searchRequestBuilder.setTypes("evaluate");
        // 设置查询的字段，String数组
        searchRequestBuilder.setFetchSource("*", null);
        searchRequestBuilder.setFrom(0);
        // 设置查询条数
        searchRequestBuilder.setSize(10);

        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        qb.mustNot(QueryBuilders.termQuery("joinField", "evaluate"));
        searchRequestBuilder.setQuery(qb);

        // 查询
        return client.search((SearchRequest) searchRequestBuilder.request(), RequestOptions.DEFAULT);
    }

    /**
     * 根据子级的条件查询父级的结构，查询结构附带子级的结构内容
     *
     * @return
     * @throws java.io.IOException
     */
    public static SearchResponse searchParentEsByChildJoin() throws IOException {

        SearchRequestBuilder searchRequestBuilder = new SearchRequestBuilder(null, SearchAction.INSTANCE);
        // 设置索引
        searchRequestBuilder.setIndices("bjcomment");
        // 设置type
        searchRequestBuilder.setTypes("bjcomment");
        // 设置查询的字段，String数组
        searchRequestBuilder.setFetchSource("*", null);
        searchRequestBuilder.setFrom(0);
        // 设置查询条数
        searchRequestBuilder.setSize(10);
        // 排序
        searchRequestBuilder.addSort("serialNumber", SortOrder.ASC);

        // 根据子级的条件查询父级的结构；innerHit表示查询结果附带子级的结构
        QueryBuilder qb = JoinQueryBuilders
                .hasChildQuery("additionalComment",
                        QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("content", "追评文字内容")), ScoreMode.None)
                .innerHit(new InnerHitBuilder());
        searchRequestBuilder.setQuery(qb);

        // 查询
        return client.search((SearchRequest) searchRequestBuilder.request(), RequestOptions.DEFAULT);
    }

//    /**
//     * 遍历实体类的属性名称与值,将其转化成InsertQuery
//     *
//     * @param obj
//     * @param insertQuery
//     * @return
//     */
//    public static InsertQuery reflectToInsertQuery(Object obj, InsertQuery insertQuery) {
//        // 循环遍历obj实体中的属性与值
//        for (Field field : obj.getClass().getDeclaredFields()) {
//            // 设置可以获取私人属性
//            field.setAccessible(true);
//            if ("serialVersionUID".equals(field.getName())) {
//                continue;
//            }
//            try {
//                @SuppressWarnings("rawtypes")
//                // 得到此属性的类型
//                        Class type = field.getType();
//                if (type == String.class) {
//                    if (field.get(obj) != null) {
//                        insertQuery.addString(field.getName(), field.get(obj) + "");
//                    }
//                }
//                if (type == Integer.class || type == int.class) {
//                    if (field.get(obj) != null) {
//                        insertQuery.addInt(field.getName(), (Integer) field.get(obj));
//                    }
//                }
//                if (type == Long.class || type == long.class) {
//                    if (field.get(obj) != null) {
//                        insertQuery.addLong(field.getName(), (Long) field.get(obj));
//                    }
//                }
//                if (type == Date.class) {
//                    if (field.get(obj) != null) {
//                        insertQuery.addString(field.getName(),
//                                DateUtil.dateToString((Date) field.get(obj), "yyyy-MM-dd HH:mm:ss"));
//                    }
//                }
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//        return insertQuery;
//    }
//
//    /**
//     * 遍历实体类的属性名称与值,将其转化成UpdateQuery
//     *
//     * @param obj
//     * @param updateQuery
//     * @return
//     */
//    public static UpdateQuery reflectToUpdateQuery(Object obj, UpdateQuery updateQuery) {
//        // 循环遍历obj实体中的属性与值
//        for (Field field : obj.getClass().getDeclaredFields()) {
//            // 设置可以获取私人属性
//            field.setAccessible(true);
//            if ("serialVersionUID".equals(field.getName())) {
//                continue;
//            }
//            try {
//                @SuppressWarnings("rawtypes")
//                // 得到此属性的类型
//                        Class type = field.getType();
//                if (type == String.class) {
//                    if (field.get(obj) != null) {
//                        updateQuery.addString(field.getName(), replaceAllEnter(field.get(obj) + ""));
//                    }
//                }
//                if (type == Integer.class || type == int.class) {
//                    if (field.get(obj) != null) {
//                        updateQuery.addInt(field.getName(), (Integer) field.get(obj));
//                    }
//                }
//                if (type == Long.class || type == long.class) {
//                    if (field.get(obj) != null) {
//                        updateQuery.addLong(field.getName(), (Long) field.get(obj));
//                    }
//                }
//                if (type == Date.class) {
//                    if (field.get(obj) != null) {
//                        updateQuery.addString(field.getName(),
//                                DateUtil.dateToString((Date) field.get(obj), "yyyy-MM-dd HH:mm:ss"));
//                    }
//                }
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//        return updateQuery;
//    }

    // =======================更新相关的方法==========================

    /**
     * 更新方法
     */
    public static boolean update(EsQueryVo vo) {
        UpdateRequest doc = new UpdateRequest(vo.getIndices(), vo.getTypes(), vo.getPkValue()).script(vo.getScript());
        try {
            if (!StringUtils.isEmpty(vo.getPkValue())) {// 根据主键进行更新
                return true;
            } else if (vo.getScript() != null && vo.getBoolQuery() != null) {// 根据查询条件更新
                return updateByQuery(vo);
            } else {
                return false;
            }
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * 根据查询条件进行更新
     */
    public static boolean updateByQuery(EsQueryVo vo) {
        try {
            UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest(new String[]{vo.getIndices()});
            updateByQueryRequest.setAbortOnVersionConflict(false);
            updateByQueryRequest.setRefresh(true);
            updateByQueryRequest.setQuery(vo.getBoolQuery());
            updateByQueryRequest.setScript(vo.getScript());
            client.updateByQuery(updateByQueryRequest, RequestOptions.DEFAULT);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * map转化成Script
     */
    public static Script mapToScript(Map<String, Object> map) {
        StringBuffer buffer = new StringBuffer();
        // 遍历map集合
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Integer) {
                buffer.append("ctx._source." + entry.getKey() + "=" + value + ";");
            } else {
                buffer.append("ctx._source." + entry.getKey() + "='" + replaceAllEsStr(value + "") + "';");
            }
        }
        if (buffer.length() > 0) {
            return new Script(buffer.toString());
        } else {
            return null;
        }
    }



}
