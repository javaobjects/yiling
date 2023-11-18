package com.yiling.search.goods.service.impl;

import java.util.List;
import java.util.Optional;


import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.framework.common.enums.SortEnum;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.search.config.EsIndexConfig;
import com.yiling.search.goods.dao.EsGoodsRepository;
import com.yiling.search.goods.dto.request.EsActivityGoodsSearchRequest;
import com.yiling.search.goods.dto.request.EsGoodsInventoryIndexRequest;
import com.yiling.search.goods.dto.request.EsGoodsSearchRequest;
import com.yiling.search.goods.entity.EsGoodsEntity;
import com.yiling.search.goods.service.EsGoodsSearchService;
import com.yiling.search.util.EsEntityUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @date: 2021/6/10 <br>
 * @author: fei.wu <br>
 */
@Service
@Slf4j
public class EsGoodsSearchServiceImpl implements EsGoodsSearchService {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private EsGoodsRepository esGoodsRepository;

    @Autowired
    private EsIndexConfig esIndexConfig;

    @Override
    public EsAggregationDTO<GoodsInfoDTO> searchGoods(EsGoodsSearchRequest request) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

//        addTermQuery(boolQueryBuilder, "gc_name1", request.getGcName1());
//        addTermQuery(boolQueryBuilder, "gc_name2", request.getGcName2());
        addTermQuery(boolQueryBuilder, "manufacturer.manufacturer_keyword", request.getManufacturer());
//        addTermQuery(boolQueryBuilder, "dosage_name", request.getDosageName());

        if (request.getMallFlag() != null) {
            addTermQuery(boolQueryBuilder, "mall_flag", request.getMallFlag());
        }
        if (request.getPopFlag() != null) {
            addTermQuery(boolQueryBuilder, "pop_flag", request.getPopFlag());
        }
        if (request.getMallStatus() != null) {
            addTermQuery(boolQueryBuilder, "mall_status", request.getMallStatus());
        }
        if (request.getPopStatus() != null) {
            addTermQuery(boolQueryBuilder, "pop_status", request.getPopStatus());
        }
        if (request.getAuditStatus() != null) {
            addTermQuery(boolQueryBuilder, "audit_status", request.getAuditStatus());
        }


        if (CollUtil.isNotEmpty(request.getEids())) {
            TermsQueryBuilder termsQueryBuilder = new TermsQueryBuilder("eid", request.getEids());
            boolQueryBuilder.must(termsQueryBuilder);
        }

        if (StringUtils.isNotBlank(request.getKey())) {
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("name", request.getKey());
            matchQueryBuilder.minimumShouldMatch("75%");
            boolQueryBuilder.must(matchQueryBuilder);
        }

        if (StringUtils.isNotBlank(request.getEname())) {
            MultiMatchQueryBuilder multiMatchQueryBuilder = new MultiMatchQueryBuilder(request.getEname(), "ename", "ename.pinyin");
            boolQueryBuilder.must(multiMatchQueryBuilder);
        }

        if (request.getHasB2bStock() != null && request.getHasB2bStock().intValue() == 1) {
            addTermQuery(boolQueryBuilder, "has_b2b_stock", 1);
        }

        if (CollUtil.isNotEmpty(request.getExcludeEids())) {
            TermsQueryBuilder termsQueryBuilder = new TermsQueryBuilder("eid", request.getExcludeEids());
            boolQueryBuilder.mustNot(termsQueryBuilder);
        }
        builder.withQuery(boolQueryBuilder);
        TermsAggregationBuilder aggTerm3 = AggregationBuilders.terms("agg_manufacturer").field("manufacturer.manufacturer_keyword");
        TermsAggregationBuilder aggTerm6 = AggregationBuilders.terms("agg_gdf_name").field("gdf_name");
        builder.addAggregation(aggTerm3).addAggregation(aggTerm6);
        if(StringUtils.isNotBlank(request.getCollapseField())){
            builder.withCollapseField(request.getCollapseField());
        }
        builder.withPageable(PageRequest.of(request.getCurrent() - 1,request.getSize()));

        //先按条件排序，若无条件则按分数排序，若分数相同则按id排序
        if (StringUtils.isNotBlank(request.getSortField())) {
            if (SortEnum.ASC.equals(request.getSortEnum())) {
                builder.withSort(SortBuilders.fieldSort(request.getSortField()).order(SortOrder.ASC));
            } else {
                builder.withSort(SortBuilders.fieldSort(request.getSortField()).order(SortOrder.DESC));
            }
        } else {
            builder.withSort(SortBuilders.fieldSort("_score").order(SortOrder.DESC));
        }
        builder.withSort(SortBuilders.fieldSort("id").order(SortOrder.ASC));
        SearchHits<EsGoodsEntity> searchHits = elasticsearchRestTemplate.search(builder.build(), EsGoodsEntity.class);
        EsAggregationDTO<GoodsInfoDTO> result = EsEntityUtils.toEsAggregation(searchHits,GoodsInfoDTO.class,request.getCurrent(),request.getSize());
        return result;
    }

    @Override
    public List<String> searchGoodsSuggest(EsGoodsSearchRequest request) {
        //提示只给前100条
        request.setCurrent(1);
        request.setSize(100);
        request.setCollapseField("name.name_keyword");
        EsAggregationDTO<GoodsInfoDTO> dto = this.searchGoods(request);
        List<String> list = Lists.newLinkedList();
        if(CollectionUtil.isNotEmpty(dto.getData())){
            dto.getData().forEach(goods->{
                list.add(goods.getName());
            });
        }
        return list;
    }


    @Override
    public EsAggregationDTO<GoodsInfoDTO> searchActivityGoods(EsActivityGoodsSearchRequest request) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withPageable(PageRequest.of(request.getCurrent() - 1,request.getSize()));

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if (request.getMallFlag() != null) {
            addTermQuery(boolQueryBuilder, "mall_flag", request.getMallFlag());
        }

        if (request.getMallStatus() != null) {
            addTermQuery(boolQueryBuilder, "mall_status", request.getMallStatus());
        }
        if (request.getAuditStatus() != null) {
            addTermQuery(boolQueryBuilder, "audit_status", request.getAuditStatus());
        }

        if (CollUtil.isNotEmpty(request.getExcludeEids())) {
            TermsQueryBuilder termsQueryBuilder = new TermsQueryBuilder("eid", request.getExcludeEids());
            boolQueryBuilder.mustNot(termsQueryBuilder);
        }

        if (StringUtils.isNotBlank(request.getKey())) {
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("combination_key", request.getKey());
            matchQueryBuilder.minimumShouldMatch("75%");
            boolQueryBuilder.must(matchQueryBuilder);
        }

        if (!request.getAllEidFlag()) {
            BoolQueryBuilder shouldQueryBuilder = new BoolQueryBuilder();
            if (CollUtil.isNotEmpty(request.getEidList())) {
                TermsQueryBuilder termsQueryBuilder = new TermsQueryBuilder("eid", request.getEidList());
                shouldQueryBuilder.should(termsQueryBuilder);
            }
            if (CollUtil.isNotEmpty(request.getGoodsIdList())) {
                TermsQueryBuilder termsQueryBuilder = new TermsQueryBuilder("id", request.getGoodsIdList());
                shouldQueryBuilder.should(termsQueryBuilder);
            }
            if (CollUtil.isNotEmpty(request.getSellSpecificationsIdList())) {
                TermsQueryBuilder termsQueryBuilder = new TermsQueryBuilder("sell_specifications_id", request.getSellSpecificationsIdList());
                shouldQueryBuilder.should(termsQueryBuilder);
            }
            boolQueryBuilder.must(shouldQueryBuilder);
        }

        builder.withQuery(boolQueryBuilder);
        if(StringUtils.isNotBlank(request.getCollapseField())){
            builder.withCollapseField(request.getCollapseField());
        }
        //优先分数降叙，分数相同则id升序
        builder.withSort(SortBuilders.fieldSort("_score").order(SortOrder.DESC));
        builder.withSort(SortBuilders.fieldSort("id").order(SortOrder.ASC));
        //指定企业靠前排序
        if(CollUtil.isNotEmpty(request.getSortEid())){
            TermsQueryBuilder sortEidTermsQuery = new TermsQueryBuilder("eid", request.getSortEid());
            FunctionScoreQueryBuilder.FilterFunctionBuilder[] filterFunctionBuilders = new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{
                    new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.boolQuery().must(sortEidTermsQuery), ScoreFunctionBuilders.weightFactorFunction(100)),
            };
            FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(boolQueryBuilder,filterFunctionBuilders);
            builder.withQuery(functionScoreQueryBuilder);
        }else {
            builder.withQuery(boolQueryBuilder);
        }

        SearchHits<EsGoodsEntity> searchHits = elasticsearchRestTemplate.search(builder.build(), EsGoodsEntity.class);
        EsAggregationDTO<GoodsInfoDTO> result = EsEntityUtils.toEsAggregation(searchHits,GoodsInfoDTO.class,request.getCurrent(),request.getSize());
        log.debug("debug ------> {} ", JSON.toJSONString(result));
        return result;
    }

    @Override
    public List<String> searchActivityGoodsSuggest(EsActivityGoodsSearchRequest request) {
        //提示只给前100条
        request.setCurrent(1);
        request.setSize(100);
        request.setCollapseField("name.name_keyword");
        EsAggregationDTO<GoodsInfoDTO> dto = this.searchActivityGoods(request);
        List<String> list = Lists.newLinkedList();
        if(CollectionUtil.isNotEmpty(dto.getData())){
            dto.getData().forEach(goods->{
                list.add(goods.getName());
            });
        }
        return list;
    }

    @Override
    public Boolean updateQty(EsGoodsInventoryIndexRequest request) {
        //先查询一下
        Optional<EsGoodsEntity> optional = esGoodsRepository.findById(String.valueOf(request.getGoodsId()));
        if (!optional.isPresent()) {
            return false;
        }
        Document document = Document.create();
        document.put("available_qty", request.getAvailableQty());
        UpdateQuery build = UpdateQuery.builder(String.valueOf(request.getGoodsId()))
                .withDocAsUpsert(true)
                .withRetryOnConflict(3)
                .withDocument(document)
                .build();
        elasticsearchRestTemplate.update(build, IndexCoordinates.of(esIndexConfig.getGoodsEsIndex()));
        //esClientService.update(goodsEsIndex, String.valueOf(request.getGoodsId()), map);
        return true;
    }

    @Override
    public Boolean updateQtyFlag(EsGoodsInventoryIndexRequest request) {
        //先查询一下
        Optional<EsGoodsEntity> optional = esGoodsRepository.findById(String.valueOf(request.getGoodsId()));
        if (!optional.isPresent()) {
            return false;
        }
        if(!request.getHasB2bStock().equals(optional.get().getHasB2bStock())){
            Document document = Document.create();
            document.put("has_b2b_stock", request.getHasB2bStock());
            UpdateQuery build = UpdateQuery.builder(String.valueOf(request.getGoodsId()))
                    .withDocAsUpsert(true)
                    .withRetryOnConflict(3)
                    .withDocument(document)
                    .build();
            elasticsearchRestTemplate.update(build, IndexCoordinates.of(esIndexConfig.getGoodsEsIndex()));
        }
        return true;
    }


    @Override
    public Boolean creatIndex() {
        boolean exist = elasticsearchRestTemplate.indexOps(EsGoodsEntity.class).exists();
        if(!exist){
            //索引创建
            elasticsearchRestTemplate.indexOps(EsGoodsEntity.class).create();
            //更新mapping
            Document mapping = elasticsearchRestTemplate.indexOps(EsGoodsEntity.class).createMapping(EsGoodsEntity.class);
            elasticsearchRestTemplate.indexOps(EsGoodsEntity.class).putMapping(mapping);
        }
        return true;
    }

    private void addTermQuery(BoolQueryBuilder builder, String field, Object value) {
        addTermQuery(builder, field, value, false);
    }

    private void addTermQuery(BoolQueryBuilder builder, String field, Object value, boolean should) {
        if (value != null) {
            if (should) {
                if (value instanceof List && value != null && ((List) value).size() > 0) {
                    builder.should(new TermsQueryBuilder(field, (List) value));
                }
                if (value instanceof Integer && value != null) {
                    builder.should(new TermsQueryBuilder(field, value.toString()));
                }
                if (value instanceof Long && value != null) {
                    builder.should(new TermsQueryBuilder(field, value.toString()));
                }
                if (value instanceof String && value != null && ((String) value).length() > 0) {
                    builder.should(new TermQueryBuilder(field, value.toString()));
                }
            } else {
                if (value instanceof List && value != null && ((List) value).size() > 0) {
                    builder.must(new TermsQueryBuilder(field, (List) value));
                }
                if (value instanceof Integer && value != null) {
                    builder.must(new TermsQueryBuilder(field, value.toString()));
                }
                if (value instanceof Long && value != null) {
                    builder.must(new TermsQueryBuilder(field, value.toString()));
                }
                if (value instanceof String && value != null && ((String) value).length() > 0) {
                    builder.must(new TermQueryBuilder(field, value.toString()));
                }
            }
        }
    }
}
