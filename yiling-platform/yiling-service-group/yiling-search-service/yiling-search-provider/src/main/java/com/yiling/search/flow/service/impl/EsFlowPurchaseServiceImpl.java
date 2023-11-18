package com.yiling.search.flow.service.impl;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.search.config.EsIndexConfig;
import com.yiling.search.flow.dao.EsFlowPurchaseRepository;
import com.yiling.search.flow.dto.EsFlowPurchaseDTO;
import com.yiling.search.flow.dto.EsScrollDTO;
import com.yiling.search.flow.dto.request.EsFlowPurchaseScrollRequest;
import com.yiling.search.flow.dto.request.EsFlowPurchaseSearchRequest;
import com.yiling.search.flow.dto.request.QueryScrollRequest;
import com.yiling.search.flow.entity.EsFlowPurchaseEntity;
import com.yiling.search.flow.service.EsFlowPurchaseService;
import com.yiling.search.util.EsEntityUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchScrollHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

/**
 * @author: shuang.zhang
 * @date: 2023/2/13
 */
@Service
@Slf4j
public class EsFlowPurchaseServiceImpl implements EsFlowPurchaseService {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private EsFlowPurchaseRepository esFlowPurchaseRepository;

    @Resource
    private EsIndexConfig esIndexConfig;

    @Override
    public Boolean updateFlowPurchase(List<EsFlowPurchaseEntity> request) {
        esFlowPurchaseRepository.saveAll(request);
        return true;
    }

    @Override
    public EsAggregationDTO<EsFlowPurchaseDTO> searchFlowPurchase(EsFlowPurchaseSearchRequest request) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if (request.getDelFlag() != null) {
            boolQueryBuilder.must(new TermsQueryBuilder("del_flag", request.getDelFlag().toString()));
        }
        if (request.getEid() != null) {
            boolQueryBuilder.must(new TermsQueryBuilder("eid", request.getEid().toString()));
        }
        if (request.getSupplierLevel() != null) {
            boolQueryBuilder.must(new TermsQueryBuilder("supplier_level", request.getSupplierLevel().toString()));
        }
        if (request.getDataTags() != null&&request.getDataTags().size()>0) {
            boolQueryBuilder.must(new TermsQueryBuilder("data_tag", request.getDataTags()));
        }
        if(StrUtil.isNotEmpty(request.getEnterpriseName())) {
            boolQueryBuilder.must(new MatchQueryBuilder("enterprise_name",request.getEnterpriseName()).operator(Operator.AND));
        }
        if (StrUtil.isNotEmpty(request.getGoodsName())) {
            boolQueryBuilder.must(new MatchQueryBuilder("goods_name", request.getGoodsName()).operator(Operator.AND));
        }
        if (StrUtil.isNotEmpty(request.getPoSpecifications())) {
            boolQueryBuilder.must(new MatchQueryBuilder("po_specifications",request.getPoSpecifications()).operator(Operator.AND));
        }
        if (request.getStartTime() != null&&request.getEndTime()!=null) {
            boolQueryBuilder.must(new RangeQueryBuilder("po_time").from(request.getStartTime().getTime()).to(request.getEndTime().getTime()));
        }
        BoolQueryBuilder shouldBuilder= new BoolQueryBuilder();
        if (CollUtil.isNotEmpty(request.getProvinceCodes())) {
            shouldBuilder.should(new TermsQueryBuilder("province_code", request.getProvinceCodes()));
        }
        if (request.getEids()!=null&&request.getEids().size()>0) {
            shouldBuilder.should(new TermsQueryBuilder("eid", request.getEids()));
        }
        boolQueryBuilder.must(shouldBuilder);
        //优先分数降叙，分数相同则id升序
        NativeSearchQuery builder = new NativeSearchQueryBuilder()
        .withPageable(PageRequest.of(request.getCurrent() - 1,request.getSize()))
        .withQuery(boolQueryBuilder)
        .withSort(SortBuilders.fieldSort("po_time").order(SortOrder.DESC))
        .withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC)).build();
        builder.setTrackTotalHits(true);

        SearchHits<EsFlowPurchaseEntity> searchHits = elasticsearchRestTemplate.search(builder, EsFlowPurchaseEntity.class);
        EsAggregationDTO<EsFlowPurchaseDTO> result = EsEntityUtils.toEsAggregation(searchHits,EsFlowPurchaseDTO.class,request.getCurrent(),request.getSize());
        log.debug("debug ------> {} ", JSON.toJSONString(result));
        return result;
    }

    @Override
    public EsScrollDTO<EsFlowPurchaseDTO> scrollFlowPurchaseFirst(EsFlowPurchaseScrollRequest request) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if (request.getDelFlag() != null) {
            boolQueryBuilder.must(new TermsQueryBuilder("del_flag", request.getDelFlag().toString()));
        }
        if (request.getEid() != null) {
            boolQueryBuilder.must(new TermsQueryBuilder("eid", request.getEid().toString()));
        }
        if (request.getSupplierLevel() != null) {
            boolQueryBuilder.must(new TermsQueryBuilder("supplier_level", request.getSupplierLevel().toString()));
        }
        if (request.getDataTags() != null&&request.getDataTags().size()>0) {
            boolQueryBuilder.must(new TermsQueryBuilder("data_tag", request.getDataTags()));
        }
        if(StrUtil.isNotEmpty(request.getEnterpriseName())) {
            boolQueryBuilder.must(new MatchQueryBuilder("enterprise_name",request.getEnterpriseName()).operator(Operator.AND));
        }
        if (StrUtil.isNotEmpty(request.getGoodsName())) {
            boolQueryBuilder.must(new MatchQueryBuilder("goods_name", request.getGoodsName()).operator(Operator.AND));
        }
        if (StrUtil.isNotEmpty(request.getPoSpecifications())) {
            boolQueryBuilder.must(new MatchQueryBuilder("po_specifications",request.getPoSpecifications()).operator(Operator.AND));
        }
        if (request.getStartTime() != null&&request.getEndTime()!=null) {
            boolQueryBuilder.must(new RangeQueryBuilder("po_time").from(request.getStartTime().getTime()).to(request.getEndTime().getTime()));
        }
        BoolQueryBuilder shouldBuilder= new BoolQueryBuilder();
        if (CollUtil.isNotEmpty(request.getProvinceCodes())) {
            shouldBuilder.should(new TermsQueryBuilder("province_code", request.getProvinceCodes()));
        }
        if (request.getEids()!=null&&request.getEids().size()>0) {
            shouldBuilder.should(new TermsQueryBuilder("eid", request.getEids()));
        }
        boolQueryBuilder.must(shouldBuilder);
        //优先分数降叙，分数相同则id升序
        NativeSearchQuery builder = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withSort(SortBuilders.fieldSort("po_time").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC)).build();
        builder.setMaxResults(request.getSize());

        // 1、缓存第一页符合搜索条件的数据
        SearchScrollHits<EsFlowPurchaseEntity> searchScrollHits = elasticsearchRestTemplate.searchScrollStart(request.getScrollTimeInMillis(), builder, EsFlowPurchaseEntity.class, IndexCoordinates.of(esIndexConfig.getFlowPurchaseEsIndex()));
        // 2、是否有命中数据
        if (searchScrollHits.hasSearchHits()) {
            return EsEntityUtils.toEsScroll(searchScrollHits, EsFlowPurchaseDTO.class);
        }
        return null;
    }

    @Override
    public EsScrollDTO<EsFlowPurchaseDTO> scrollFlowPurchaseContinue(QueryScrollRequest request) {
        if (request == null) {
            return null;
        }
        SearchScrollHits<EsFlowPurchaseEntity> searchScrollHits = elasticsearchRestTemplate.searchScrollContinue(request.getScrollId(), request.getScrollTimeInMillis(), EsFlowPurchaseEntity.class, IndexCoordinates.of(esIndexConfig.getFlowPurchaseEsIndex()));
        // 2、是否有命中数据
        if (searchScrollHits.hasSearchHits()) {
            return EsEntityUtils.toEsScroll(searchScrollHits, EsFlowPurchaseDTO.class);
        } else {
            elasticsearchRestTemplate.searchScrollClear(Arrays.asList(request.getScrollId()));
        }
        return null;
    }
}
