package com.yiling.search.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.ParsedAvg;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.base.EsBaseEntity;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 EsClientUtils
 * @描述
 * @创建时间 2022/8/25
 * @修改人 shichen
 * @修改时间 2022/8/25
 **/
@Slf4j
@Component
public class EsClientUtils {


    @Resource
    private RestHighLevelClient restHighLevelClient;

    /**
     * Description: 判断某个index是否存在
     */
    public boolean indexExist(String index) {
        try {
            GetIndexRequest request = new GetIndexRequest(index);
            request.local(false);
            request.humanReadable(true);
            request.includeDefaults(false);
            return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("[EsClientService][indexExist] 异常！ 参数:" + index + " " + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED, "搜索引擎异常。");
        }

    }

    /**
     * Description: 插入/更新一条记录
     */
    public void saveOrUpdate(String index, EsBaseEntity entity) {
        IndexRequest request = new IndexRequest(index);
        request.id(String.valueOf(entity.getId()));
        request.source(JSON.toJSONString(entity), XContentType.JSON);
        try {
            restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("[EsClientService][saveOrUpdate] 异常！参数: " + index + " " + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED, "搜索引擎异常。");
        }
    }

    public void update(String index, String id, Map<String, Object> map) {
        UpdateRequest update = new UpdateRequest().retryOnConflict(3).docAsUpsert(true);
        update.index(index);
        update.id(id);
        update.doc(map);
        try {
            restHighLevelClient.update(update, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("[EsClientService][update] 异常！参数: " + index + " " + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED, "搜索引擎异常。");
        }
    }

    public Map<String, Object> getIndex(String index, String id) {
        GetRequest getRequest = new GetRequest();
        getRequest.index(index);
        getRequest.id(id);
        try {
            GetResponse getResponse=restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
            Map<String, Object> source = getResponse.getSource();
            return source;
        } catch (Exception e) {
            log.error("[EsClientService][getIndex] 异常！参数: " + index + " " + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED, "搜索引擎异常。");
        }
    }

    public void updateBatch(String index, List<Map<String, Object>> list) {
        BulkRequest request = new BulkRequest();
        list.forEach(item -> request.add(new UpdateRequest(index, item.get("goodsId").toString()).doc((Map<String, Object>)item.get("params"))));
        try {
            restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("[EsClientService][updateBatch] 异常！参数: " + index + " " + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED, "搜索引擎异常。");
        }
    }


    /**
     * Description: 批量插入数据
     */
    public void insertBatch(String index, List<EsBaseEntity> list) {
        BulkRequest request = new BulkRequest();
        list.forEach(item -> request.add(new IndexRequest(index).id(String.valueOf(item.getId()))
                .source(JSON.toJSONString(item), XContentType.JSON)));
        try {
            restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("[EsClientService][insertBatch] 异常！参数: " + index + " " + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED, "搜索引擎异常。");
        }
    }

    /**
     * Description: 批量删除
     */
    public <T> void deleteBatch(String index, Collection<T> idList) {
        BulkRequest request = new BulkRequest();
        idList.forEach(item -> request.add(new DeleteRequest(index, item.toString())));
        try {
            restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("[EsClientService][deleteBatch] 异常！参数: " + index + " " + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED, "搜索引擎异常。");
        }
    }

    public List<String> completion(String index, String key) {
        try {
            SearchRequest request = new SearchRequest(index);

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            SuggestionBuilder termSuggestionBuilder =
                    SuggestBuilders.completionSuggestion("keyword_standard").skipDuplicates(true).text(key);
            SuggestBuilder suggestBuilder = new SuggestBuilder();
            suggestBuilder.addSuggestion("search_key_suggest", termSuggestionBuilder);
            searchSourceBuilder.suggest(suggestBuilder);
            request.source(searchSourceBuilder);
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            Suggest suggest = response.getSuggest();
            CompletionSuggestion termSuggestion = suggest.getSuggestion("search_key_suggest");
            List<String> res = new ArrayList<>();
            for (CompletionSuggestion.Entry entry : termSuggestion.getEntries()) {
                for (CompletionSuggestion.Entry.Option option : entry) {
                    String suggestText = option.getText().string();
                    res.add(suggestText);
                }
            }
            return res;
        } catch (Exception e) {
            log.error("[EsClientService][suggest] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED, "搜索引擎异常。");
        }


    }

    /**
     * Description: 搜索
     *
     * @param index   index
     * @param builder 查询参数
     * @param c       结果类对象
     */
    public <T> List<T> search(String index, SearchSourceBuilder builder, Class<T> c) {
        SearchRequest request = new SearchRequest(index);
        request.source(builder);
        try {
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            SearchHit[] hits = response.getHits().getHits();
            List<T> res = new ArrayList<>(hits.length);
            for (SearchHit hit : hits) {
                res.add(JSON.parseObject(hit.getSourceAsString(), c));
            }
            return res;
        } catch (Exception e) {
            log.error("[EsUtil][search] 异常！参数: " + index + " " + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED, "搜索引擎异常。");
        }
    }


    public <T> EsAggregationDTO<T> searchHasAggregation(String index, SearchSourceBuilder builder, Class<T> c) {
        SearchRequest request = new SearchRequest(index);

        request.source(builder);
        EsAggregationDTO<T> aggDto = new EsAggregationDTO<>();
        try {
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            SearchHit[] hits = response.getHits().getHits();

            Aggregations aggregations = response.getAggregations();
            if(aggregations!=null) {
                Map<String, Aggregation> asMap = aggregations.getAsMap();

                Map<String, Object> bucketMap = new HashMap<>();
                for (String key : asMap.keySet()) {
                    Aggregation aggregation = asMap.get(key);

                    //不同类型 aggregation 自定义解析方法。可以根据需要添加
                    if (aggregation instanceof ParsedAvg) {
                        ParsedAvg avg = (ParsedAvg) aggregation;
                        HashMap<String, Object> valueMap = new HashMap<>(1);
                        valueMap.put(avg.getType(), String.valueOf(avg.getValue()));
                        bucketMap.put(key, valueMap);
                    }

                    if (aggregation instanceof ParsedStringTerms) {
                        List<? extends Terms.Bucket> buckets = ((ParsedStringTerms) aggregation).getBuckets();
                        List tmp = new ArrayList(buckets.size());
                        for (Terms.Bucket bucket : buckets) {
                            String name = bucket.getKey().toString();
                            if (StringUtils.isBlank(name)) {continue;}
                            HashMap<String, Long> valueMap = new HashMap<>(1);
                            valueMap.put(name, bucket.getDocCount());
                            tmp.add(valueMap);
                        }
                        bucketMap.put(key, tmp);
                    }

                    if (aggregation instanceof ParsedLongTerms) {
                        List<? extends Terms.Bucket> buckets = ((ParsedLongTerms) aggregation).getBuckets();
                        List tmp = new ArrayList(buckets.size());
                        for (Terms.Bucket bucket : buckets) {
                            String name = bucket.getKey().toString();
                            if (StringUtils.isBlank(name)) {continue;}
                            HashMap<String, Long> valueMap = new HashMap<>(1);
                            valueMap.put(name, bucket.getDocCount());
                            tmp.add(valueMap);
                        }
                        bucketMap.put(key, tmp);
                    }
                }
                aggDto.setAggregation(bucketMap);
            }

            List<T> res = new ArrayList<>(hits.length);
            for (SearchHit hit : hits) {
                res.add(JSON.parseObject(hit.getSourceAsString(), c));
            }
            aggDto.setTotal(response.getHits().getTotalHits().value);
            aggDto.setData(res);
            return aggDto;
        } catch (Exception e) {
            log.error("[EsUtil][searchHasAggregation] 异常！参数: " + index + " " + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED, "搜索引擎异常。");
        }
    }

    /**
     * Description: delete by query
     */
    public void deleteByQuery(String index, QueryBuilder builder) {
        DeleteByQueryRequest request = new DeleteByQueryRequest(index);
        request.setQuery(builder);
        //设置批量操作数量,最大为10000
        request.setBatchSize(10000);
        request.setConflicts("proceed");
        try {
            restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("[EsUtil][deleteByQuery] 异常！参数: " + index + " " + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED, "搜索引擎异常。");
        }
    }

    /**
     * 刷新索引
     * @param indexName
     * @return
     * @author shichen
     */
    public boolean indexRefresh(String ...indexName ) {
        log.info("ES索引开始刷新，索引名为："+indexName.toString());
        try {

            RefreshResponse response =  restHighLevelClient.indices()
                    .refresh(new RefreshRequest(indexName),RequestOptions.DEFAULT);

            if(response.getShardFailures().length>0){
                log.error("[EsUtil][updateByQuery] 异常！参数: " + indexName + " " + response.getShardFailures());
                return false;
            }
            log.info("ES索引刷新成功");
            return true;
        }catch (Exception e) {
            log.error("[EsUtil][indexRefresh] 异常！参数: " + indexName + " " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * 更新索引数据
     * @param indexName
     * @return
     * @author shichen
     */
    public Boolean updateByQuery(String indexName,boolean asyncFlag){
        UpdateByQueryRequest updateByQuery  = new UpdateByQueryRequest(indexName);
        //设置版本冲突时继续执行
        updateByQuery.setConflicts("proceed");
        //版本冲突中止关闭，不关词库更新索引历史数据无效
        updateByQuery.setAbortOnVersionConflict(false);
        //设置更新完成后刷新索引 ps很重要如果不加可能数据不会实时刷新
        updateByQuery.setRefresh(true);
        try {
            if(asyncFlag){
                restHighLevelClient.updateByQueryAsync(updateByQuery, RequestOptions.DEFAULT,new ActionListener<BulkByScrollResponse>() {
                    @Override
                    public void onResponse(BulkByScrollResponse bulkResponse) {
                        if(bulkResponse.getSearchFailures().size()>0){
                            log.error("[EsUtil][updateByQuery] 异步执行异常！参数: " + indexName + " " + bulkResponse.getBulkFailures());
                        }
                    }
                    @Override
                    public void onFailure(Exception e) {
                        log.error("[EsUtil][updateByQuery] 异步执行异常！参数: " + indexName + " " + e.getMessage());
                    }});
            }else {
                BulkByScrollResponse response = restHighLevelClient.
                        updateByQuery(updateByQuery,RequestOptions.DEFAULT);
                restHighLevelClient.updateByQuery(updateByQuery,RequestOptions.DEFAULT);
                if(response.getSearchFailures().size()>0){
                    log.error("[EsUtil][updateByQuery] 异常！参数: " + indexName + " " + response.getBulkFailures());
                    return false;
                }
            }

        } catch (Exception e) {
            log.error("[EsUtil][updateByQuery] 异常！参数: " + indexName + " " + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED, "搜索引擎异常。");
        }
        return true;
    }
}
