package com.yiling.search.flow.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.hutool.core.date.DateUtil;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.search.BaseTest;
import com.yiling.search.flow.dto.EsFlowEnterpriseGoodsMappingDTO;
import com.yiling.search.flow.dto.EsFlowSaleDTO;
import com.yiling.search.flow.dto.EsScrollDTO;
import com.yiling.search.flow.dto.request.EsFlowEnterpriseGoodsMappingSearchRequest;
import com.yiling.search.flow.dto.request.EsFlowGoodsBatchDetailSearchRequest;
import com.yiling.search.flow.dto.request.EsFlowSaleScrollRequest;
import com.yiling.search.flow.dto.request.EsFlowSaleSearchRequest;
import com.yiling.search.flow.dto.request.QueryScrollRequest;
import com.yiling.search.flow.entity.EsFlowSaleEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: fei.wu <br>
 * @date: 2021/6/11 <br>
 */
@Slf4j
public class EsFlowSaleSearchServiceTest extends BaseTest {


    @Autowired
    private EsFlowSaleService esFlowSaleService;
    @Autowired
    private EsFlowGoodsBatchDetailService esFlowGoodsBatchDetailService;

    @Autowired
    private EsFlowEnterpriseGoodsMappingService esFlowEnterpriseGoodsMappingService;

    @Test
    public void test1() {
        List<EsFlowSaleEntity> request=new ArrayList<>();
        EsFlowSaleEntity esFlowSaleEntity=new EsFlowSaleEntity();
        esFlowSaleEntity.setId(4L);
        esFlowSaleEntity.setCityCode(4L);
        esFlowSaleEntity.setCrmCode("4");
        esFlowSaleEntity.setCrmGoodsCode(4L);
        esFlowSaleEntity.setEid(4L);
        esFlowSaleEntity.setEname("4");
        esFlowSaleEntity.setDelFlag(0);
        esFlowSaleEntity.setCreateTime(new Date());
        esFlowSaleEntity.setUpdateTime(new Date());
        request.add(esFlowSaleEntity);
        System.out.println(esFlowSaleService.updateFlowSale(request));
    }

    @Test
    public void test2() {
        EsFlowSaleSearchRequest request=new EsFlowSaleSearchRequest();
//        request.setEnterpriseName("西平县柏国医药");
//        request.setGoodsName("连花清瘟");
        request.setStartTime(DateUtil.parse("2023-03-01","yyyy-MM-dd"));
        request.setEndTime(DateUtil.parse("2023-03-31","yyyy-MM-dd"));
//        request.setEids(Arrays.asList(208551L));
        request.setCurrent(1);
        request.setSize(100);
        esFlowSaleService.searchFlowSale(request);
    }

    @Test
    public void test31() {
        EsFlowSaleScrollRequest request=new EsFlowSaleScrollRequest();
//        request.setEnterpriseName("西平县柏国医药");
//        request.setGoodsName("连花清瘟");
        request.setStartTime(DateUtil.parse("2023-03-01","yyyy-MM-dd"));
        request.setEndTime(DateUtil.parse("2023-03-31","yyyy-MM-dd"));
//        request.setEids(Arrays.asList(208551L));
        request.setSize(100);
        EsScrollDTO<EsFlowSaleDTO> list= esFlowSaleService.scrollFlowSaleFirst(request);
        while(list!=null&& StrUtil.isNotEmpty(list.getScrollId())){
            QueryScrollRequest request1=new QueryScrollRequest();
            request1.setScrollId(list.getScrollId());
            list=esFlowSaleService.scrollFlowSaleContinue(request1);
            JSON.toJSONString(list);
        }
    }

    @Test
    public void test21() {
        EsFlowGoodsBatchDetailSearchRequest request=new EsFlowGoodsBatchDetailSearchRequest();
        request.setGoodsName("连花清瘟");
        request.setStartTime(DateUtil.parse("2023-01-01","yyyy-MM-dd"));
        request.setEndTime(new Date());
//        request.setEid(1270L);
        request.setEids(Arrays.asList(1L));
        request.setProvinceCodes(Arrays.asList("330000"));//
        request.setCurrent(1);
        request.setSize(10);
        esFlowGoodsBatchDetailService.searchFlowGoodsBatchDetail(request);
    }

    @Test
    public void test3(){
        EsFlowEnterpriseGoodsMappingSearchRequest request=new EsFlowEnterpriseGoodsMappingSearchRequest();
        request.setCrmGoodsCode(15L);
        request.setCrmEnterpriseId(200L);
        EsAggregationDTO<EsFlowEnterpriseGoodsMappingDTO> aggregationDTO = esFlowEnterpriseGoodsMappingService.searchFlowEnterpriseGoodsMapping(request);
        System.out.println(aggregationDTO);
    }

}
