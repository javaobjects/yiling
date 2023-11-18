package com.yiling.search.flow.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.framework.common.enums.SortEnum;
import com.yiling.search.flow.dao.EsFlowEnterpriseCustomerMappingRepository;
import com.yiling.search.flow.dto.EsFlowEnterpriseCustomerMappingDTO;
import com.yiling.search.flow.dto.request.EsFlowEnterpriseCustomerMappingSearchRequest;
import com.yiling.search.flow.entity.EsFlowEnterpriseCustomerMappingEntity;
import com.yiling.search.flow.service.EsFlowEnterpriseCustomerMappingService;
import com.yiling.search.util.EsEntityUtils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 EsFlowEnterpriseCustomerMappingServiceImpl
 * @描述
 * @创建时间 2023/3/1
 * @修改人 shichen
 * @修改时间 2023/3/1
 **/
@Service
@Slf4j
public class EsFlowEnterpriseCustomerMappingServiceImpl implements EsFlowEnterpriseCustomerMappingService {
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private EsFlowEnterpriseCustomerMappingRepository esFlowEnterpriseCustomerMappingRepository;

    @Override
    public EsAggregationDTO<EsFlowEnterpriseCustomerMappingDTO> searchFlowEnterpriseCustomerMapping(EsFlowEnterpriseCustomerMappingSearchRequest request) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withPageable(PageRequest.of(request.getCurrent() - 1,request.getSize()));
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(new TermsQueryBuilder("del_flag", "0"));
        if (StrUtil.isNotEmpty(request.getFlowCustomerName())) {
            boolQueryBuilder.must(new MatchQueryBuilder("flow_customer_name",request.getFlowCustomerName()).operator(Operator.AND));
        }
        if (request.getCrmOrgId()!=null) {
            //request.getCrmOrgId() == 0 时搜索未映射数据
            boolQueryBuilder.must(new TermsQueryBuilder("crm_org_id", request.getCrmOrgId().toString()));
        }else {
            boolQueryBuilder.must(new RangeQueryBuilder("crm_org_id").gt("0"));
            if (StrUtil.isNotEmpty(request.getOrgName())) {
                boolQueryBuilder.must(new MatchQueryBuilder("org_name",request.getOrgName()).operator(Operator.AND));
            }
        }
        if (request.getCrmEnterpriseId()!=null && request.getCrmEnterpriseId()>0) {
            boolQueryBuilder.must(new TermsQueryBuilder("crm_enterprise_id", request.getCrmEnterpriseId().toString()));
        }
        //部分权限鉴权
        if(request.getOrgDatascope()==2){
            if(CollectionUtil.isEmpty(request.getCrmEnterpriseIds()) && CollectionUtil.isEmpty(request.getProvinceCodes())){
                EsAggregationDTO aggregationDTO = new EsAggregationDTO();
                aggregationDTO.setCurrent(request.getCurrent());
                aggregationDTO.setSize(request.getSize());
                return aggregationDTO;
            }
            BoolQueryBuilder shouldQueryBuilder = new BoolQueryBuilder();
            if(CollectionUtil.isNotEmpty(request.getCrmEnterpriseIds())){
                TermsQueryBuilder termsQueryBuilder = new TermsQueryBuilder("crm_enterprise_id", request.getCrmEnterpriseIds());
                shouldQueryBuilder.should(termsQueryBuilder);
            }
            if(CollectionUtil.isNotEmpty(request.getProvinceCodes())){
                TermsQueryBuilder termsQueryBuilder = new TermsQueryBuilder("province_code", request.getProvinceCodes());
                shouldQueryBuilder.should(termsQueryBuilder);
            }
            boolQueryBuilder.must(shouldQueryBuilder);
        }
        if (StrUtil.isNotEmpty(request.getEnterpriseName())) {
            boolQueryBuilder.must(new MatchQueryBuilder("enterprise_name",request.getEnterpriseName()).operator(Operator.AND));
        }
        if (StrUtil.isNotEmpty(request.getProvinceCode())) {
            boolQueryBuilder.must(new MatchQueryBuilder("province_code",request.getProvinceCode()).operator(Operator.AND));
        }
        if (request.getStartUpdateTime() != null&&request.getEndUpdateTime()!=null) {
            boolQueryBuilder.must(new RangeQueryBuilder("update_time").from(request.getStartUpdateTime().getTime()).to(request.getEndUpdateTime().getTime()));
        }
        if (request.getStartLastUploadTime() != null&&request.getEndLastUploadTime()!=null) {
            boolQueryBuilder.must(new RangeQueryBuilder("last_upload_time").from(request.getStartLastUploadTime().getTime()).to(request.getEndLastUploadTime().getTime()));
        }
        if (request.getRecommendOrgCrmId() != null) {
            boolQueryBuilder.must(new MatchQueryBuilder("recommend_org_crm_id", request.getRecommendOrgCrmId()).operator(Operator.AND));
        }
        if (request.getRecommendFlag() != null && request.getRecommendFlag() == 1) {
            boolQueryBuilder.must(new RangeQueryBuilder("recommend_org_crm_id").gt("0"));
        }
        builder.withQuery(boolQueryBuilder);

        builder.withSort(EsEntityUtils.searchSort(request.getSortField(),request.getSortEnum()));
        NativeSearchQuery searcBuild = builder.build();
        searcBuild.setTrackTotalHits(true);
        SearchHits<EsFlowEnterpriseCustomerMappingEntity> searchHits = elasticsearchRestTemplate.search(searcBuild, EsFlowEnterpriseCustomerMappingEntity.class);
        EsAggregationDTO<EsFlowEnterpriseCustomerMappingDTO> result = EsEntityUtils.toEsAggregation(searchHits, EsFlowEnterpriseCustomerMappingDTO.class, request.getCurrent(), request.getSize());
        log.debug("debug ------> {} ", JSON.toJSONString(result));
        return result;
    }
}
