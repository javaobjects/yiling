package com.yiling.search.flow.service.impl;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.search.config.EsIndexConfig;
import com.yiling.search.flow.dao.EsFlowSaleRepository;
import com.yiling.search.flow.dto.EsFlowSaleDTO;
import com.yiling.search.flow.dto.EsScrollDTO;
import com.yiling.search.flow.dto.request.EsFlowSaleScrollRequest;
import com.yiling.search.flow.dto.request.EsFlowSaleSearchRequest;
import com.yiling.search.flow.dto.request.QueryScrollRequest;
import com.yiling.search.flow.entity.EsFlowSaleEntity;
import com.yiling.search.flow.service.EsFlowSaleService;
import com.yiling.search.util.EsEntityUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
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
public class EsFlowSaleServiceImpl implements EsFlowSaleService {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private EsFlowSaleRepository esFlowSaleRepository;

    @Resource
    private EsIndexConfig esIndexConfig;

    @Override
    public Boolean updateFlowSale(List<EsFlowSaleEntity> request) {
        esFlowSaleRepository.saveAll(request);
        return true;
    }

    @Override
    public EsAggregationDTO<EsFlowSaleDTO> searchFlowSale(EsFlowSaleSearchRequest request) {
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
        if (request.getDataTags() != null && request.getDataTags().size() > 0) {
            boolQueryBuilder.must(new TermsQueryBuilder("data_tag", request.getDataTags()));
        }
        if (StrUtil.isNotEmpty(request.getEnterpriseName())) {
            boolQueryBuilder.must(new MatchQueryBuilder("enterprise_name", request.getEnterpriseName()).operator(Operator.AND));
        }
        if (StrUtil.isNotEmpty(request.getGoodsName())) {
            boolQueryBuilder.must(new MatchQueryBuilder("goods_name", request.getGoodsName()).operator(Operator.AND));
        }
        if (StrUtil.isNotEmpty(request.getSoSpecifications())) {
            boolQueryBuilder.must(new MatchQueryBuilder("so_specifications", request.getSoSpecifications()).operator(Operator.AND));
        }
        if (request.getStartTime() != null && request.getEndTime() != null) {
            boolQueryBuilder.must(new RangeQueryBuilder("so_time").from(request.getStartTime().getTime()).to(request.getEndTime().getTime()));
        }
        BoolQueryBuilder shouldBuilder = new BoolQueryBuilder();
        if (CollUtil.isNotEmpty(request.getProvinceCodes())) {
            shouldBuilder.should(new TermsQueryBuilder("province_code", request.getProvinceCodes()));
        }
        if (request.getEids() != null && request.getEids().size() > 0) {
            shouldBuilder.should(new TermsQueryBuilder("eid", request.getEids()));
        }
        boolQueryBuilder.must(shouldBuilder);
        //优先分数降叙，分数相同则id升序
        NativeSearchQuery search = new NativeSearchQueryBuilder()
                .withPageable(PageRequest.of(request.getCurrent() - 1, request.getSize()))
                .withQuery(boolQueryBuilder)
                .withSort(SortBuilders.fieldSort("_score").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("so_time").order(SortOrder.DESC)).build();
        search.setTrackTotalHits(true);

        SearchHits<EsFlowSaleEntity> searchHits = elasticsearchRestTemplate.search(search, EsFlowSaleEntity.class);
        EsAggregationDTO<EsFlowSaleDTO> result = EsEntityUtils.toEsAggregation(searchHits, EsFlowSaleDTO.class, request.getCurrent(), request.getSize());
        log.debug("debug ------> {} ", JSON.toJSONString(result));
        return result;
    }

    @Override
    public EsScrollDTO<EsFlowSaleDTO> scrollFlowSaleFirst(EsFlowSaleScrollRequest request) {
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
        if (request.getDataTags() != null && request.getDataTags().size() > 0) {
            boolQueryBuilder.must(new TermsQueryBuilder("data_tag", request.getDataTags()));
        }
        if (StrUtil.isNotEmpty(request.getEnterpriseName())) {
            boolQueryBuilder.must(new MatchQueryBuilder("enterprise_name", request.getEnterpriseName()).operator(Operator.AND));
        }
        if (StrUtil.isNotEmpty(request.getGoodsName())) {
            boolQueryBuilder.must(new MatchQueryBuilder("goods_name", request.getGoodsName()).operator(Operator.AND));
        }
        if (StrUtil.isNotEmpty(request.getSoSpecifications())) {
            boolQueryBuilder.must(new MatchQueryBuilder("so_specifications", request.getSoSpecifications()).operator(Operator.AND));
        }
        if (request.getStartTime() != null && request.getEndTime() != null) {
            boolQueryBuilder.must(new RangeQueryBuilder("so_time").from(request.getStartTime().getTime()).to(request.getEndTime().getTime()));
        }
        BoolQueryBuilder shouldBuilder = new BoolQueryBuilder();
        if (CollUtil.isNotEmpty(request.getProvinceCodes())) {
            shouldBuilder.should(new TermsQueryBuilder("province_code", request.getProvinceCodes()));
        }
        if (request.getEids() != null && request.getEids().size() > 0) {
            shouldBuilder.should(new TermsQueryBuilder("eid", request.getEids()));
        }
        boolQueryBuilder.must(shouldBuilder);
        //优先分数降叙，分数相同则id升序
        NativeSearchQuery search = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withSort(SortBuilders.fieldSort("so_time").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC))
                .build();
        search.setMaxResults(request.getSize());

        // 1、缓存第一页符合搜索条件的数据
        SearchScrollHits<EsFlowSaleEntity> searchScrollHits = elasticsearchRestTemplate.searchScrollStart(request.getScrollTimeInMillis(), search, EsFlowSaleEntity.class, IndexCoordinates.of(esIndexConfig.getFlowSaleEsIndex()));
        // 2、是否有命中数据
        if (searchScrollHits.hasSearchHits()) {
            return EsEntityUtils.toEsScroll(searchScrollHits, EsFlowSaleDTO.class);
        }
        return null;
    }

    @Override
    public EsScrollDTO<EsFlowSaleDTO> scrollFlowSaleContinue(QueryScrollRequest request) {
        if (request == null) {
            return null;
        }
        SearchScrollHits<EsFlowSaleEntity> searchScrollHits = elasticsearchRestTemplate.searchScrollContinue(request.getScrollId(), request.getScrollTimeInMillis(), EsFlowSaleEntity.class, IndexCoordinates.of(esIndexConfig.getFlowSaleEsIndex()));
        // 2、是否有命中数据
        if (searchScrollHits.hasSearchHits()) {
            return EsEntityUtils.toEsScroll(searchScrollHits, EsFlowSaleDTO.class);
        } else {
            elasticsearchRestTemplate.searchScrollClear(Arrays.asList(request.getScrollId()));
        }
        return null;
    }
}
