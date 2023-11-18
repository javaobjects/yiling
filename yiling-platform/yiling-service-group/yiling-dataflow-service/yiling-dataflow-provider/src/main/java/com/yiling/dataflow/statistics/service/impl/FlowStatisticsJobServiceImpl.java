package com.yiling.dataflow.statistics.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.dto.FlowGoodsBatchDetailDTO;
import com.yiling.dataflow.order.dto.FlowPurchaseDTO;
import com.yiling.dataflow.order.dto.FlowSaleDTO;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchDetailListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;
import com.yiling.dataflow.order.dto.request.SaveOrUpdateFlowGoodsBatchDetailRequest;
import com.yiling.dataflow.order.dto.request.SaveOrUpdateFlowPurchaseRequest;
import com.yiling.dataflow.order.dto.request.SaveOrUpdateFlowSaleRequest;
import com.yiling.dataflow.order.entity.FlowGoodsSpecMappingDO;
import com.yiling.dataflow.order.service.FlowGoodsBatchDetailService;
import com.yiling.dataflow.order.service.FlowGoodsSpecMappingService;
import com.yiling.dataflow.order.service.FlowPurchaseService;
import com.yiling.dataflow.order.service.FlowSaleService;
import com.yiling.dataflow.report.dto.request.DeteleTimeRequest;
import com.yiling.dataflow.report.dto.request.SaveReportFlowGoodsBatchRequest;
import com.yiling.dataflow.report.dto.request.SaveReportFlowPopPurchaseRequest;
import com.yiling.dataflow.report.dto.request.SaveReportFlowPurchaseRequest;
import com.yiling.dataflow.report.dto.request.SaveReportFlowSaleRequest;
import com.yiling.dataflow.report.service.ReportFlowGoodsBatchService;
import com.yiling.dataflow.report.service.ReportFlowPopPurchaseService;
import com.yiling.dataflow.report.service.ReportFlowPurchaseService;
import com.yiling.dataflow.report.service.ReportFlowSaleService;
import com.yiling.dataflow.statistics.dto.ErpClientDataDTO;
import com.yiling.dataflow.statistics.service.FlowStatisticsJobService;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.ylprice.api.GoodsYilingPriceApi;
import com.yiling.goods.ylprice.dto.GoodsYilingPriceDTO;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.dto.OrderDeliveryReportCountDTO;
import com.yiling.order.order.dto.request.QueryOrderDeliveryReportRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2022/6/13
 */
@Slf4j
@Service
public class FlowStatisticsJobServiceImpl implements FlowStatisticsJobService {

    @Autowired
    private FlowSaleService flowSaleService;

    @Autowired
    private FlowPurchaseService flowPurchaseService;

    @Autowired
    private FlowGoodsBatchDetailService flowGoodsBatchDetailService;

    @Autowired
    private ReportFlowSaleService reportFlowSaleService;

    @Autowired
    private ReportFlowPurchaseService reportFlowPurchaseService;

    @Autowired
    private ReportFlowPopPurchaseService reportFlowPopPurchaseService;

    @Autowired
    private ReportFlowGoodsBatchService reportFlowGoodsBatchService;

    @Autowired
    private FlowGoodsSpecMappingService flowGoodsSpecMappingService;

    @DubboReference
    private OrderApi orderApi;

    @DubboReference(timeout = 1000 * 60)
    private EnterpriseApi enterpriseApi;

    @DubboReference
    private GoodsYilingPriceApi goodsYilingPriceApi;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private List<String> strList = Arrays.asList("连锁", "药店", "药房");

    @Override
    public void statisticsFlowJob(List<ErpClientDataDTO> erpClientDataDTOList) {
        //添加erpClient流向任务
        try {

            Date time = DateUtil.offsetDay(DateUtil.parseDate(DateUtil.today()), -1);

            DeteleTimeRequest deteleTimeRequest = new DeteleTimeRequest();
            deteleTimeRequest.setTime(DateUtil.offsetDay(time, -44));
            reportFlowSaleService.deleteReportFlowSale(deteleTimeRequest);
            reportFlowPurchaseService.deleteReportFlowPurchase(deteleTimeRequest);
            reportFlowPopPurchaseService.deleteReportFlowPopPurchase(deteleTimeRequest);
            reportFlowGoodsBatchService.deleteReportFlowGoodsBatch(deteleTimeRequest);
            String key = RedisKey.generate("flow", "enterpriseAll");
            stringRedisTemplate.delete(key);
            List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(erpClientDataDTOList.stream().map(e -> e.getRkSuId()).collect(Collectors.toList()));
            //渠道企业
            QueryEnterprisePageListRequest enterpriseRequest = new QueryEnterprisePageListRequest();
            enterpriseRequest.setStatus(1);
            //需要循环调用
            Page<EnterpriseDTO> enterprisePage = null;
            int enterpriseCurrent = 1;
            do {
                enterpriseRequest.setCurrent(enterpriseCurrent);
                enterpriseRequest.setSize(2000);
                enterprisePage = enterpriseApi.pageList(enterpriseRequest);
                if (enterprisePage == null || CollUtil.isEmpty(enterprisePage.getRecords())) {
                    break;
                }
                Map<String, List<EnterpriseDTO>> enterpriseListMapByName = enterprisePage.getRecords().stream().collect(Collectors.groupingBy(EnterpriseDTO::getName));
                Map<String, String> enterpriseMap = new HashMap<>();
                for (Map.Entry<String, List<EnterpriseDTO>> map : enterpriseListMapByName.entrySet()) {
                    enterpriseMap.put(map.getKey(), JSON.toJSONString(map.getValue()));
                }
                stringRedisTemplate.opsForHash().putAll(key, enterpriseMap);
                enterpriseCurrent = enterpriseCurrent + 1;
            } while (enterprisePage != null && CollUtil.isNotEmpty(enterprisePage.getRecords()));


            for (EnterpriseDTO enterpriseDTO : enterpriseDTOList) {
                statisticsSale(time, enterpriseDTO);
                statisticsPurchase(time, enterpriseDTO);
                statisticsGoodsBatch(time, enterpriseDTO);
            }
            log.info("流向统计完成");
        } catch (Exception e) {
            log.error("流向统计异常", e);
        }
    }

    private void statisticsGoodsBatch(Date time, EnterpriseDTO enterprise) {
        try {
            QueryFlowGoodsBatchDetailListPageRequest goodsBatchDetailRequest = new QueryFlowGoodsBatchDetailListPageRequest();
            goodsBatchDetailRequest.setEidList(Arrays.asList(enterprise.getId()));
            goodsBatchDetailRequest.setStartTime(DateUtil.offsetDay(time, -44));
            goodsBatchDetailRequest.setEndTime(DateUtil.endOfDay(time));
            List<FlowGoodsBatchDetailDTO> goodsBatchFlowExcelList = new ArrayList<>();
            Page<FlowGoodsBatchDetailDTO> pageSale = null;
            int currentSale = 1;
            do {
                goodsBatchDetailRequest.setCurrent(currentSale);
                goodsBatchDetailRequest.setSize(2000);
                pageSale = PojoUtils.map(flowGoodsBatchDetailService.page(goodsBatchDetailRequest), FlowGoodsBatchDetailDTO.class);

                if (pageSale == null || CollUtil.isEmpty(pageSale.getRecords())) {
                    break;
                }

                goodsBatchFlowExcelList.addAll(pageSale.getRecords());
                currentSale = currentSale + 1;
            } while (pageSale != null && CollUtil.isNotEmpty(pageSale.getRecords()));

            if (CollUtil.isNotEmpty(goodsBatchFlowExcelList)) {
                //  根据specId 获取商品商销价
                List<Long> specificationIdList = goodsBatchFlowExcelList.stream().map(FlowGoodsBatchDetailDTO::getSpecificationId).filter(specificationId -> specificationId != 0).collect(Collectors.toList());
                List<GoodsYilingPriceDTO> goodsYilingPriceDTOList = goodsYilingPriceApi.getPriceBySpecificationIdList(specificationIdList, 3L, DateUtil.parse(DateUtil.today()));

                Map<String, SaveReportFlowGoodsBatchRequest> goodsBatchMap = new HashMap<>();
                List<SaveOrUpdateFlowGoodsBatchDetailRequest> saveOrUpdateFlowGoodsBatchDetailRequestList = new ArrayList<>();
                for (FlowGoodsBatchDetailDTO flowGoodsBatchDetailDTO : goodsBatchFlowExcelList) {
                    Integer type = 2;
                    if (flowGoodsBatchDetailDTO.getGbName().contains("连花")) {
                        type = 1;
                    }
                    Long eid = flowGoodsBatchDetailDTO.getEid();
                    if (enterprise.getChannelId() == null || enterprise.getChannelId() == 0) {
                        SaveOrUpdateFlowGoodsBatchDetailRequest saveOrUpdateFlowSaleRequest = new SaveOrUpdateFlowGoodsBatchDetailRequest();
                        saveOrUpdateFlowSaleRequest.setId(flowGoodsBatchDetailDTO.getId());
                        saveOrUpdateFlowSaleRequest.setRemark("3");
                        saveOrUpdateFlowGoodsBatchDetailRequestList.add(saveOrUpdateFlowSaleRequest);
                        continue;
                    }

                    FlowGoodsSpecMappingDO flowGoodsSpecMappingDO = flowGoodsSpecMappingService.findByGoodsNameAndSpec(flowGoodsBatchDetailDTO.getGbName(), flowGoodsBatchDetailDTO.getGbSpecifications(),flowGoodsBatchDetailDTO.getGbManufacturer());

                    if (flowGoodsSpecMappingDO == null) {
                        SaveOrUpdateFlowGoodsBatchDetailRequest saveOrUpdateFlowSaleRequest = new SaveOrUpdateFlowGoodsBatchDetailRequest();
                        saveOrUpdateFlowSaleRequest.setId(flowGoodsBatchDetailDTO.getId());
                        saveOrUpdateFlowSaleRequest.setRemark("1");
                        saveOrUpdateFlowGoodsBatchDetailRequestList.add(saveOrUpdateFlowSaleRequest);
                        continue;
                    }

                    BigDecimal price = goodsYilingPriceDTOList.stream()
                            .filter(g -> g.getSpecificationId().equals(flowGoodsSpecMappingDO.getSpecificationId())).findAny()
                            .map(GoodsYilingPriceDTO::getPrice).orElse(new BigDecimal(0));

                    if (price.intValue() <= 0) {
                        SaveOrUpdateFlowGoodsBatchDetailRequest saveOrUpdateFlowSaleRequest = new SaveOrUpdateFlowGoodsBatchDetailRequest();
                        saveOrUpdateFlowSaleRequest.setId(flowGoodsBatchDetailDTO.getId());
                        saveOrUpdateFlowSaleRequest.setRemark("2");
                        saveOrUpdateFlowGoodsBatchDetailRequestList.add(saveOrUpdateFlowSaleRequest);
                        continue;
                    }

                    //查询报表是否存在
                    StringBuffer sb = new StringBuffer();
                    sb.append(eid).append("-").append(type).append("-").append(DateUtil.format(flowGoodsBatchDetailDTO.getGbDetailTime(), "yyyy-MM-dd"));
                    if (goodsBatchMap.containsKey(sb.toString())) {
                        SaveReportFlowGoodsBatchRequest saveReportFlowGoodsBatchRequest = goodsBatchMap.get(sb.toString());
                        Long soQuantity = saveReportFlowGoodsBatchRequest.getGbQuantity() + flowGoodsBatchDetailDTO.getGbNumber().longValue();
                        BigDecimal totalAmount = price.multiply(new BigDecimal(soQuantity));
                        saveReportFlowGoodsBatchRequest.setTotalAmount(totalAmount);
                        saveReportFlowGoodsBatchRequest.setGbQuantity(soQuantity);
                    } else {
                        SaveReportFlowGoodsBatchRequest saveReportFlowSaleRequest = new SaveReportFlowGoodsBatchRequest();
                        saveReportFlowSaleRequest.setEid(eid);
                        saveReportFlowSaleRequest.setEname(flowGoodsBatchDetailDTO.getEname());
                        saveReportFlowSaleRequest.setGoodsCategory(type);
                        saveReportFlowSaleRequest.setGbTime(DateUtil.parse(DateUtil.format(flowGoodsBatchDetailDTO.getGbDetailTime(), "yyyy-MM-dd"), "yyyy-MM-dd"));
                        saveReportFlowSaleRequest.setGbQuantity(flowGoodsBatchDetailDTO.getGbNumber().longValue());
                        saveReportFlowSaleRequest.setYlSalePrice(price);
                        saveReportFlowSaleRequest.setTotalAmount(price.multiply(new BigDecimal(saveReportFlowSaleRequest.getGbQuantity())));
                        goodsBatchMap.put(sb.toString(), saveReportFlowSaleRequest);
                    }
                }
                flowGoodsBatchDetailService.updateFlowGoodsBatchDetailByIds(saveOrUpdateFlowGoodsBatchDetailRequestList);
                reportFlowGoodsBatchService.saveReportFlowGoodsBatchList(new ArrayList<>(goodsBatchMap.values()));
            }
        } catch (Exception e) {
            log.error("库存流向统计失败eid={}", enterprise.getId(), e);
        }
    }

    private void statisticsSale(Date time, EnterpriseDTO enterprise) {
        try {
            QueryFlowPurchaseListPageRequest request = new QueryFlowPurchaseListPageRequest();
            request.setEidList(Arrays.asList(enterprise.getId()));
            request.setStartTime(DateUtil.offsetDay(time, -44));
            request.setEndTime(DateUtil.endOfDay(time));
            List<FlowSaleDTO> saleFlowExcelList = new ArrayList<>();
            Page<FlowSaleDTO> pageSale = null;
            int currentSale = 1;
            do {
                request.setCurrent(currentSale);
                request.setSize(2000);
                pageSale = PojoUtils.map(flowSaleService.page(request), FlowSaleDTO.class);

                if (pageSale == null || CollUtil.isEmpty(pageSale.getRecords())) {
                    break;
                }
                saleFlowExcelList.addAll(pageSale.getRecords());
                currentSale = currentSale + 1;
            } while (pageSale != null && CollUtil.isNotEmpty(pageSale.getRecords()));

            if (CollUtil.isNotEmpty(saleFlowExcelList)) {
                List<Long> specificationIdList = saleFlowExcelList.stream().map(FlowSaleDTO::getSpecificationId).filter(specificationId -> specificationId != 0).collect(Collectors.toList());
                List<GoodsYilingPriceDTO> goodsYilingPriceDTOList = goodsYilingPriceApi.getPriceBySpecificationIdList(specificationIdList, 3L, DateUtil.parse(DateUtil.today()));

                Map<String, SaveReportFlowSaleRequest> map = new HashMap<>();
                List<SaveOrUpdateFlowSaleRequest> saveOrUpdateFlowSaleRequestList = new ArrayList<>();
                for (FlowSaleDTO flowSaleDTO : saleFlowExcelList) {
                    Integer type = 2;
                    if (flowSaleDTO.getGoodsName().contains("连花")) {
                        type = 1;
                    }

                    if (enterprise.getChannelId() == null || enterprise.getChannelId() == 0) {
                        SaveOrUpdateFlowSaleRequest saveOrUpdateFlowSaleRequest = new SaveOrUpdateFlowSaleRequest();
                        saveOrUpdateFlowSaleRequest.setId(flowSaleDTO.getId());
                        saveOrUpdateFlowSaleRequest.setRemark("3");
                        saveOrUpdateFlowSaleRequestList.add(saveOrUpdateFlowSaleRequest);
                        continue;
                    }


                    FlowGoodsSpecMappingDO flowGoodsSpecMappingDO = flowGoodsSpecMappingService.findByGoodsNameAndSpec(flowSaleDTO.getGoodsName(), flowSaleDTO.getSoSpecifications(),flowSaleDTO.getSoManufacturer());
                    if (flowGoodsSpecMappingDO == null) {
                        SaveOrUpdateFlowSaleRequest saveOrUpdateFlowSaleRequest = new SaveOrUpdateFlowSaleRequest();
                        saveOrUpdateFlowSaleRequest.setId(flowSaleDTO.getId());
                        saveOrUpdateFlowSaleRequest.setRemark("1");
                        saveOrUpdateFlowSaleRequestList.add(saveOrUpdateFlowSaleRequest);
                        continue;
                    }

                    BigDecimal price = goodsYilingPriceDTOList.stream()
                            .filter(g -> g.getSpecificationId().equals(flowGoodsSpecMappingDO.getSpecificationId())).findAny()
                            .map(GoodsYilingPriceDTO::getPrice).orElse(new BigDecimal(0));

                    if (price.intValue() <= 0) {
                        SaveOrUpdateFlowSaleRequest saveOrUpdateFlowSaleRequest = new SaveOrUpdateFlowSaleRequest();
                        saveOrUpdateFlowSaleRequest.setId(flowSaleDTO.getId());
                        saveOrUpdateFlowSaleRequest.setRemark("2");
                        saveOrUpdateFlowSaleRequestList.add(saveOrUpdateFlowSaleRequest);
                        continue;
                    }

                    String key = RedisKey.generate("flow", "enterpriseAll");
                    //销售流向卖给同一渠道不算
                    List<EnterpriseDTO> enterpriseDTOList = null;
                    Object enterpriseJson = stringRedisTemplate.opsForHash().get(key, StrUtil.trim(flowSaleDTO.getEnterpriseName()));
                    if (enterpriseJson != null) {
                        enterpriseDTOList = JSON.parseArray(String.valueOf(enterpriseJson), EnterpriseDTO.class);
                        if (CollUtil.isNotEmpty(enterpriseDTOList)) {
                            if (enterpriseDTOList.get(0).getChannelId().equals(enterprise.getChannelId())) {
                                SaveOrUpdateFlowSaleRequest saveOrUpdateFlowSaleRequest = new SaveOrUpdateFlowSaleRequest();
                                saveOrUpdateFlowSaleRequest.setId(flowSaleDTO.getId());
                                saveOrUpdateFlowSaleRequest.setRemark("4");
                                saveOrUpdateFlowSaleRequestList.add(saveOrUpdateFlowSaleRequest);
                                continue;
                            }
                        }
                    }

                    StringBuffer sb = new StringBuffer();
                    sb.append(enterprise.getId()).append("-").append(type).append("-").append(DateUtil.format(flowSaleDTO.getSoTime(), "yyyy-MM-dd"));
                    if (map.containsKey(sb.toString())) {
                        SaveReportFlowSaleRequest saveReportFlowSaleRequest = map.get(sb.toString());
                        Long soQuantity = saveReportFlowSaleRequest.getSoQuantity() + flowSaleDTO.getSoQuantity().longValue();
                        BigDecimal totalAmount = price.multiply(new BigDecimal(soQuantity));
                        saveReportFlowSaleRequest.setSoQuantity(soQuantity);
                        saveReportFlowSaleRequest.setTotalAmount(totalAmount);
                        boolean bool = isStatisticsSale(enterpriseDTOList, flowSaleDTO.getEnterpriseName(), enterprise);
                        BigDecimal terminalAmount = BigDecimal.ZERO;
                        Long terminalQuantity = 0L;
                        if (bool) {
                            terminalQuantity = saveReportFlowSaleRequest.getTerminalQuantity() + flowSaleDTO.getSoQuantity().longValue();

                            terminalAmount = price.multiply(new BigDecimal(terminalQuantity));
                            saveReportFlowSaleRequest.setTerminalQuantity(terminalQuantity);
                            saveReportFlowSaleRequest.setTerminalAmount(terminalAmount);
                        }

                    } else {
                        SaveReportFlowSaleRequest saveReportFlowSaleRequest = new SaveReportFlowSaleRequest();
                        saveReportFlowSaleRequest.setEid(flowSaleDTO.getEid());
                        saveReportFlowSaleRequest.setEname(flowSaleDTO.getEname());
                        saveReportFlowSaleRequest.setGoodsCategory(type);
                        saveReportFlowSaleRequest.setSoTime(DateUtil.parse(DateUtil.format(flowSaleDTO.getSoTime(), "yyyy-MM-dd"), "yyyy-MM-dd"));
                        saveReportFlowSaleRequest.setSoQuantity(flowSaleDTO.getSoQuantity().longValue());
                        saveReportFlowSaleRequest.setYlSalePrice(price);
                        saveReportFlowSaleRequest.setTotalAmount(price.multiply(flowSaleDTO.getSoQuantity()));
                        boolean bool = isStatisticsSale(enterpriseDTOList, flowSaleDTO.getEnterpriseName(), enterprise);
                        BigDecimal terminalAmount = BigDecimal.ZERO;
                        Long terminalQuantity = 0L;
                        if (bool) {
                            terminalQuantity = flowSaleDTO.getSoQuantity().longValue();
                            terminalAmount = price.multiply(flowSaleDTO.getSoQuantity());
                        }
                        saveReportFlowSaleRequest.setTerminalAmount(terminalAmount);
                        saveReportFlowSaleRequest.setTerminalQuantity(terminalQuantity);
                        map.put(sb.toString(), saveReportFlowSaleRequest);
                    }
                }
                flowSaleService.updateFlowSaleByIds(saveOrUpdateFlowSaleRequestList);
                reportFlowSaleService.saveReportFlowSaleList(new ArrayList<>(map.values()));
            }
        } catch (Exception e) {
            log.error("销售流向统计失败eid={}", enterprise.getId(), e);
        }
    }

    private boolean isStatisticsSale(List<EnterpriseDTO> enterpriseDTOList, String enterpriseName, EnterpriseDTO enterpriseDTO) {
        if (CollUtil.isEmpty(enterpriseDTOList)) {
            if (StrUtil.isNotEmpty(enterpriseName)) {
                for (String str : strList) {
                    if (enterpriseName.contains(str)) {
                        return true;
                    }
                }
            }
        } else {
            if (!enterpriseDTOList.get(0).getChannelId().equals(enterpriseDTO.getChannelId())) {
                return true;
            }
        }
        return false;
    }

    private void statisticsPurchase(Date time, EnterpriseDTO enterprise) {
        try {
            QueryFlowPurchaseListPageRequest request = new QueryFlowPurchaseListPageRequest();
            request.setEidList(Arrays.asList(enterprise.getId()));
            request.setStartTime(DateUtil.offsetDay(time, -44));
            request.setEndTime(DateUtil.endOfDay(time));
            List<FlowPurchaseDTO> purchaseFlowExcelList = new ArrayList<>();
            Page<FlowPurchaseDTO> pagePurchase = null;
            int currentSale = 1;
            do {
                request.setCurrent(currentSale);
                request.setSize(2000);
                pagePurchase = PojoUtils.map(flowPurchaseService.page(request), FlowPurchaseDTO.class);

                if (pagePurchase == null || CollUtil.isEmpty(pagePurchase.getRecords())) {
                    break;
                }
                purchaseFlowExcelList.addAll(pagePurchase.getRecords());
                currentSale = currentSale + 1;
            } while (pagePurchase != null && CollUtil.isNotEmpty(pagePurchase.getRecords()));


            QueryOrderDeliveryReportRequest reportRequest = new QueryOrderDeliveryReportRequest();
            reportRequest.setEidList(Arrays.asList(enterprise.getId()));
            reportRequest.setStartDeliverTime(DateUtil.offsetDay(time, -44));
            reportRequest.setEndDeliverTime(DateUtil.endOfDay(time));
            List<OrderDeliveryReportCountDTO> orderDeliveryReportCountDTOList = orderApi.getOrderDeliveryReportCount(reportRequest);
            Map<String, OrderDeliveryReportCountDTO> map = new HashMap<>();
            if (CollUtil.isNotEmpty(orderDeliveryReportCountDTOList)) {
                for (OrderDeliveryReportCountDTO orderDeliveryReportCountDTO : orderDeliveryReportCountDTOList) {
                    map.put(DateUtil.format(orderDeliveryReportCountDTO.getCreateTime(), "yyyy-MM-dd"), orderDeliveryReportCountDTO);
                }
            }
            Map<String, SaveReportFlowPurchaseRequest> purchaseMap = new HashMap<>();
            Map<String, SaveReportFlowPopPurchaseRequest> popPurchaseMap = new HashMap<>();
            List<SaveOrUpdateFlowPurchaseRequest> saveOrUpdateFlowPurchaseRequestList = new ArrayList<>();

            List<GoodsYilingPriceDTO> goodsYilingPriceDTOList = new ArrayList<>();
            if (CollUtil.isNotEmpty(purchaseFlowExcelList)) {
                List<Long> specificationIdList = purchaseFlowExcelList.stream().map(FlowPurchaseDTO::getSpecificationId).filter(specificationId -> specificationId != 0).collect(Collectors.toList());
                goodsYilingPriceDTOList = goodsYilingPriceApi.getPriceBySpecificationIdList(specificationIdList, 3L, DateUtil.parse(DateUtil.today()));
            }

            for (FlowPurchaseDTO flowPurchaseDTO : purchaseFlowExcelList) {
                if (enterprise.getChannelId() == null || enterprise.getChannelId() == 0) {
                    SaveOrUpdateFlowPurchaseRequest saveOrUpdateFlowPurchaseRequest = new SaveOrUpdateFlowPurchaseRequest();
                    saveOrUpdateFlowPurchaseRequest.setId(flowPurchaseDTO.getId());
                    saveOrUpdateFlowPurchaseRequest.setRemark("3");
                    saveOrUpdateFlowPurchaseRequestList.add(saveOrUpdateFlowPurchaseRequest);
                    continue;
                }


                FlowGoodsSpecMappingDO flowGoodsSpecMappingDO = flowGoodsSpecMappingService.findByGoodsNameAndSpec(flowPurchaseDTO.getGoodsName(), flowPurchaseDTO.getPoSpecifications(),flowPurchaseDTO.getPoManufacturer());
                if (flowGoodsSpecMappingDO == null) {
                    SaveOrUpdateFlowPurchaseRequest saveOrUpdateFlowPurchaseRequest = new SaveOrUpdateFlowPurchaseRequest();
                    saveOrUpdateFlowPurchaseRequest.setId(flowPurchaseDTO.getId());
                    saveOrUpdateFlowPurchaseRequest.setRemark("1");
                    saveOrUpdateFlowPurchaseRequestList.add(saveOrUpdateFlowPurchaseRequest);
                    continue;
                }

                BigDecimal price = goodsYilingPriceDTOList.stream()
                        .filter(g -> g.getSpecificationId().equals(flowGoodsSpecMappingDO.getSpecificationId())).findAny()
                        .map(GoodsYilingPriceDTO::getPrice).orElse(new BigDecimal(0));

                if (price.intValue() <= 0) {
                    SaveOrUpdateFlowPurchaseRequest saveOrUpdateFlowPurchaseRequest = new SaveOrUpdateFlowPurchaseRequest();
                    saveOrUpdateFlowPurchaseRequest.setId(flowPurchaseDTO.getId());
                    saveOrUpdateFlowPurchaseRequest.setRemark("2");
                    saveOrUpdateFlowPurchaseRequestList.add(saveOrUpdateFlowPurchaseRequest);
                    continue;
                }

                saveReportFlowPurchase(enterprise.getId(), flowPurchaseDTO, purchaseMap, flowGoodsSpecMappingDO, price);
                saveReportFlowPopPurchase(enterprise.getId(), flowPurchaseDTO, popPurchaseMap, flowGoodsSpecMappingDO, price);
            }
            flowPurchaseService.updateFlowPurchaseByIds(saveOrUpdateFlowPurchaseRequestList);
            reportFlowPurchaseService.saveReportFlowPurchaseList(new ArrayList<>(purchaseMap.values()));
            for (Map.Entry<String, OrderDeliveryReportCountDTO> entry : map.entrySet()) {
                StringBuffer sb = new StringBuffer();
                sb.append(enterprise.getId()).append("-").append(DateUtil.format(entry.getValue().getCreateTime(), "yyyy-MM-dd"));
                if (entry.getValue() != null) {
                    OrderDeliveryReportCountDTO orderDeliveryReportCountDTO = entry.getValue();
                    if (orderDeliveryReportCountDTO.getDeliveryAmount() == null) {
                        orderDeliveryReportCountDTO.setDeliveryAmount(BigDecimal.ZERO);
                    }
                    if (orderDeliveryReportCountDTO.getDeliveryQuantity() == null) {
                        orderDeliveryReportCountDTO.setDeliveryQuantity(0);
                    }
                    if (popPurchaseMap.containsKey(sb.toString())) {
                        SaveReportFlowPopPurchaseRequest saveReportFlowPopPurchaseRequest = popPurchaseMap.get(sb.toString());
                        Long soQuantity = saveReportFlowPopPurchaseRequest.getErpQuantity() + orderDeliveryReportCountDTO.getDeliveryQuantity().longValue();
                        BigDecimal totalAmount = saveReportFlowPopPurchaseRequest.getErpTotalAmount().add(orderDeliveryReportCountDTO.getDeliveryAmount());
                        saveReportFlowPopPurchaseRequest.setErpQuantity(soQuantity);
                        saveReportFlowPopPurchaseRequest.setErpTotalAmount(totalAmount);
                    } else {
                        SaveReportFlowPopPurchaseRequest saveReportFlowPopPurchaseRequest = new SaveReportFlowPopPurchaseRequest();
                        saveReportFlowPopPurchaseRequest.setEid(enterprise.getId());
                        saveReportFlowPopPurchaseRequest.setEname(enterprise.getName());
                        saveReportFlowPopPurchaseRequest.setPoTime(DateUtil.parse(DateUtil.format(orderDeliveryReportCountDTO.getCreateTime(), "yyyy-MM-dd"), "yyyy-MM-dd"));
                        saveReportFlowPopPurchaseRequest.setErpQuantity(orderDeliveryReportCountDTO.getDeliveryQuantity().longValue());
                        saveReportFlowPopPurchaseRequest.setErpTotalAmount(orderDeliveryReportCountDTO.getDeliveryAmount());
                        popPurchaseMap.put(sb.toString(), saveReportFlowPopPurchaseRequest);
                    }
                }
            }
            reportFlowPopPurchaseService.saveReportFlowPopPurchaseList(new ArrayList<>(popPurchaseMap.values()));
        } catch (Exception e) {
            log.error("采购流向统计失败eid={}", enterprise.getId(), e);
        }
    }

    private void saveReportFlowPurchase(Long eid, FlowPurchaseDTO flowPurchaseDTO, Map<String, SaveReportFlowPurchaseRequest> purchaseMap, FlowGoodsSpecMappingDO flowGoodsSpecMappingDO, BigDecimal price) {
        Integer type = 2;
        if (flowPurchaseDTO.getGoodsName().contains("连花")) {
            type = 1;
        }

        StringBuffer sb = new StringBuffer();
        sb.append(eid).append("-").append(type).append("-").append(DateUtil.format(flowPurchaseDTO.getPoTime(), "yyyy-MM-dd"));
        if (purchaseMap.containsKey(sb.toString())) {
            SaveReportFlowPurchaseRequest saveReportFlowPurchaseRequest = purchaseMap.get(sb.toString());
            Long soQuantity = saveReportFlowPurchaseRequest.getPoQuantity() + flowPurchaseDTO.getPoQuantity().longValue();
            BigDecimal totalAmount = price.multiply(new BigDecimal(soQuantity));
            saveReportFlowPurchaseRequest.setPoQuantity(soQuantity);
            saveReportFlowPurchaseRequest.setTotalAmount(totalAmount);
        } else {
            SaveReportFlowPurchaseRequest saveReportFlowPurchaseRequest = new SaveReportFlowPurchaseRequest();
            saveReportFlowPurchaseRequest.setEid(flowPurchaseDTO.getEid());
            saveReportFlowPurchaseRequest.setEname(flowPurchaseDTO.getEname());
            saveReportFlowPurchaseRequest.setGoodsCategory(type);
            saveReportFlowPurchaseRequest.setPoTime(DateUtil.parse(DateUtil.format(flowPurchaseDTO.getPoTime(), "yyyy-MM-dd"), "yyyy-MM-dd"));
            saveReportFlowPurchaseRequest.setPoQuantity(flowPurchaseDTO.getPoQuantity().longValue());
            saveReportFlowPurchaseRequest.setYlSalePrice(price);
            saveReportFlowPurchaseRequest.setTotalAmount(price.multiply(flowPurchaseDTO.getPoQuantity()));
            purchaseMap.put(sb.toString(), saveReportFlowPurchaseRequest);
        }
    }

    private void saveReportFlowPopPurchase(Long eid, FlowPurchaseDTO flowPurchaseDTO, Map<String, SaveReportFlowPopPurchaseRequest> popPurchaseMap, FlowGoodsSpecMappingDO flowGoodsSpecMappingDO, BigDecimal price) {
        StringBuffer sb = new StringBuffer();
        sb.append(eid).append("-").append(DateUtil.format(flowPurchaseDTO.getPoTime(), "yyyy-MM-dd"));
        if (popPurchaseMap.containsKey(sb.toString())) {
            SaveReportFlowPopPurchaseRequest saveReportFlowPopPurchaseRequest = popPurchaseMap.get(sb.toString());
            Long soQuantity = saveReportFlowPopPurchaseRequest.getPoQuantity() + flowPurchaseDTO.getPoQuantity().longValue();
            BigDecimal totalAmount = price.multiply(new BigDecimal(soQuantity));
            saveReportFlowPopPurchaseRequest.setPoQuantity(soQuantity);
            saveReportFlowPopPurchaseRequest.setPoTotalAmount(totalAmount);
        } else {
            SaveReportFlowPopPurchaseRequest saveReportFlowPopPurchaseRequest = new SaveReportFlowPopPurchaseRequest();
            saveReportFlowPopPurchaseRequest.setEid(flowPurchaseDTO.getEid());
            saveReportFlowPopPurchaseRequest.setEname(flowPurchaseDTO.getEname());
            saveReportFlowPopPurchaseRequest.setPoTime(DateUtil.parse(DateUtil.format(flowPurchaseDTO.getPoTime(), "yyyy-MM-dd"), "yyyy-MM-dd"));
            saveReportFlowPopPurchaseRequest.setPoQuantity(flowPurchaseDTO.getPoQuantity().longValue());
            saveReportFlowPopPurchaseRequest.setPoTotalAmount(price.multiply(flowPurchaseDTO.getPoQuantity()));
            saveReportFlowPopPurchaseRequest.setErpTotalAmount(BigDecimal.ZERO);
            saveReportFlowPopPurchaseRequest.setErpQuantity(0L);
            popPurchaseMap.put(sb.toString(), saveReportFlowPopPurchaseRequest);
        }
    }
}
