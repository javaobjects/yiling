package com.yiling.search.flow.service.impl;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.search.config.EsIndexConfig;
import com.yiling.search.flow.dao.EsFlowGoodsBatchDetailRepository;
import com.yiling.search.flow.dto.EsFlowGoodsBatchDetailDTO;
import com.yiling.search.flow.dto.EsFlowPurchaseDTO;
import com.yiling.search.flow.dto.EsScrollDTO;
import com.yiling.search.flow.dto.request.EsFlowGoodsBatchDetailScrollRequest;
import com.yiling.search.flow.dto.request.EsFlowGoodsBatchDetailSearchRequest;
import com.yiling.search.flow.dto.request.QueryScrollRequest;
import com.yiling.search.flow.entity.EsFlowGoodsBatchDetailEntity;
import com.yiling.search.flow.entity.EsFlowPurchaseEntity;
import com.yiling.search.flow.service.EsFlowGoodsBatchDetailService;
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
public class EsFlowGoodsBatchDetailServiceImpl implements EsFlowGoodsBatchDetailService {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private EsFlowGoodsBatchDetailRepository esFlowGoodsBatchDetailRepository;

    @Resource
    private EsIndexConfig esIndexConfig;

    @Override
    public Boolean updateFlowGoodsBatchDetail(List<EsFlowGoodsBatchDetailEntity> request) {
        esFlowGoodsBatchDetailRepository.saveAll(request);
        return true;
    }

    @Override
    public EsAggregationDTO<EsFlowGoodsBatchDetailDTO> searchFlowGoodsBatchDetail(EsFlowGoodsBatchDetailSearchRequest request) {
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
        if (StrUtil.isNotEmpty(request.getGoodsName())) {
            boolQueryBuilder.must(new MatchQueryBuilder("gb_name", request.getGoodsName()).operator(Operator.AND));
        }
        if (StrUtil.isNotEmpty(request.getPoSpecifications())) {
            boolQueryBuilder.must(new MatchQueryBuilder("gb_specifications",request.getPoSpecifications()).operator(Operator.AND));
        }
        if (request.getStartTime() != null&&request.getEndTime()!=null) {
            boolQueryBuilder.must(new RangeQueryBuilder("gb_detail_time").from(request.getStartTime().getTime()).to(request.getEndTime().getTime()));
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
        .withSort(SortBuilders.fieldSort("gb_detail_time").order(SortOrder.DESC))
        .withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC)).build();
        builder.setTrackTotalHits(true);

        SearchHits<EsFlowGoodsBatchDetailEntity> searchHits = elasticsearchRestTemplate.search(builder, EsFlowGoodsBatchDetailEntity.class);
        EsAggregationDTO<EsFlowGoodsBatchDetailDTO> result = EsEntityUtils.toEsAggregation(searchHits,EsFlowGoodsBatchDetailDTO.class,request.getCurrent(),request.getSize());
        log.debug("debug ------> {} ", JSON.toJSONString(result));
        return result;
    }

    @Override
    public EsScrollDTO<EsFlowGoodsBatchDetailDTO> scrollFlowGoodsBatchDetailFirst(EsFlowGoodsBatchDetailScrollRequest request) {
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
        if (StrUtil.isNotEmpty(request.getGoodsName())) {
            boolQueryBuilder.must(new MatchQueryBuilder("gb_name", request.getGoodsName()).operator(Operator.AND));
        }
        if (StrUtil.isNotEmpty(request.getPoSpecifications())) {
            boolQueryBuilder.must(new MatchQueryBuilder("gb_specifications",request.getPoSpecifications()).operator(Operator.AND));
        }
        if (request.getStartTime() != null&&request.getEndTime()!=null) {
            boolQueryBuilder.must(new RangeQueryBuilder("gb_detail_time").from(request.getStartTime().getTime()).to(request.getEndTime().getTime()));
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
                .withSort(SortBuilders.fieldSort("gb_detail_time").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC)).build();
        builder.setMaxResults(request.getSize());

        // 1、缓存第一页符合搜索条件的数据
        SearchScrollHits<EsFlowGoodsBatchDetailEntity> searchScrollHits = elasticsearchRestTemplate.searchScrollStart(request.getScrollTimeInMillis(), builder, EsFlowGoodsBatchDetailEntity.class, IndexCoordinates.of(esIndexConfig.getFlowEsGoodsBatchDetailIndex()));
        // 2、是否有命中数据
        if (searchScrollHits.hasSearchHits()) {
            return EsEntityUtils.toEsScroll(searchScrollHits, EsFlowGoodsBatchDetailDTO.class);
        }
        return null;
    }

    @Override
    public EsScrollDTO<EsFlowGoodsBatchDetailDTO> scrollFlowGoodsBatchDetailContinue(QueryScrollRequest request) {
        if (request == null) {
            return null;
        }
        SearchScrollHits<EsFlowGoodsBatchDetailEntity> searchScrollHits = elasticsearchRestTemplate.searchScrollContinue(request.getScrollId(), request.getScrollTimeInMillis(), EsFlowGoodsBatchDetailEntity.class, IndexCoordinates.of(esIndexConfig.getFlowEsGoodsBatchDetailIndex()));
        // 2、是否有命中数据
        if (searchScrollHits.hasSearchHits()) {
            return EsEntityUtils.toEsScroll(searchScrollHits, EsFlowGoodsBatchDetailDTO.class);
        } else {
            elasticsearchRestTemplate.searchScrollClear(Arrays.asList(request.getScrollId()));
        }
        return null;
    }
}
