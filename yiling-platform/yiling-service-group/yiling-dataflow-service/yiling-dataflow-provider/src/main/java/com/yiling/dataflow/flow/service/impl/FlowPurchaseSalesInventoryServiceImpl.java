package com.yiling.dataflow.flow.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.crm.service.CrmGoodsInfoService;
import com.yiling.dataflow.crm.service.CrmGoodsService;
import com.yiling.dataflow.flow.dao.FlowPurchaseSalesInventoryMapper;
import com.yiling.dataflow.flow.dto.request.UpdateFlowPurchaseSalesInventoryRequest;
import com.yiling.dataflow.flow.entity.FlowPurchaseSalesInventoryDO;
import com.yiling.dataflow.flow.service.FlowPurchaseSalesInventoryService;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchDetailListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;
import com.yiling.dataflow.order.entity.FlowGoodsBatchDetailDO;
import com.yiling.dataflow.order.entity.FlowPurchaseDO;
import com.yiling.dataflow.order.entity.FlowSaleDO;
import com.yiling.dataflow.order.service.FlowGoodsBatchDetailService;
import com.yiling.dataflow.order.service.FlowPurchaseService;
import com.yiling.dataflow.order.service.FlowSaleService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-11-14
 */
@Service
public class FlowPurchaseSalesInventoryServiceImpl extends BaseServiceImpl<FlowPurchaseSalesInventoryMapper, FlowPurchaseSalesInventoryDO> implements FlowPurchaseSalesInventoryService {

    @Autowired
    private FlowSaleService             flowSaleService;
    @Autowired
    private FlowPurchaseService         flowPurchaseService;
    @Autowired
    private FlowGoodsBatchDetailService flowGoodsBatchDetailService;
    @Autowired
    private CrmGoodsService             crmGoodsService;
    @Autowired
    private CrmGoodsInfoService         crmGoodsInfoService;
    @DubboReference
    private EnterpriseApi               enterpriseApi;

    @Override
    public void updateFlowPurchaseSalesInventoryByJob(UpdateFlowPurchaseSalesInventoryRequest request) {
        if (request == null) {
            return;
        }
        if (CollUtil.isEmpty(request.getEids())) {
            return;
        }

        Date startTime = null;
        Date endTime = null;
        if (request.getDateTime() == null) {
            startTime = DateUtil.beginOfMonth(new Date());
            endTime = DateUtil.endOfMonth(new Date());
        } else {
            startTime = DateUtil.beginOfMonth(request.getDateTime());
            endTime = DateUtil.endOfMonth(request.getDateTime());
        }

        Map<Long, CrmGoodsInfoDTO> crmGoodsMap = crmGoodsService.getCrmGoodsMap();
        List<Long> specIdList = crmGoodsMap.keySet().stream().map(e -> e.longValue()).collect(Collectors.toList());
        List<EnterpriseDTO> enterpriseList = enterpriseApi.listByIds(request.getEids());
        Map<Long, EnterpriseDTO> enterpriseDTOMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));
        Map<Long, List<String>> batchMap = new HashMap<>();
        //1先删除流向数据汇总数据
        for (Long eid : request.getEids()) {
//        QueryWrapper<FlowPurchaseSalesInventoryDO> queryWrapper = new QueryWrapper<>();
//        queryWrapper.lambda().in(FlowPurchaseSalesInventoryDO::getEid, request.getEids());
//        queryWrapper.lambda().eq(FlowPurchaseSalesInventoryDO::getYear,String.valueOf(DateUtil.year(startTime))).eq(FlowPurchaseSalesInventoryDO::getMonth,String.valueOf(DateUtil.month(startTime) + 1));
//        FlowPurchaseSalesInventoryDO flowPurchaseSalesInventoryDO = new FlowPurchaseSalesInventoryDO();
//        flowPurchaseSalesInventoryDO.setOpTime(new Date());
//        flowPurchaseSalesInventoryDO.setOpUserId(0L);
//        this.batchDeleteWithFill(flowPurchaseSalesInventoryDO, queryWrapper);
        this.getBaseMapper().deleteFlowPurchaseSalesInventoryByEidAndYearAndMonth(Arrays.asList(eid),String.valueOf(DateUtil.year(startTime)),String.valueOf(DateUtil.month(startTime) + 1));
        // 加载当月的销售数据
        List<FlowSaleDO> flowSaleDOList = new ArrayList<>();
        {
            QueryFlowPurchaseListPageRequest queryFlowPurchaseListPageRequest = new QueryFlowPurchaseListPageRequest();
            queryFlowPurchaseListPageRequest.setStartTime(startTime);
            queryFlowPurchaseListPageRequest.setEndTime(endTime);
            queryFlowPurchaseListPageRequest.setSpecificationIdFlag(1);
            queryFlowPurchaseListPageRequest.setEidList(Arrays.asList(eid));

            Page<FlowSaleDO> page;
            int current = 1;
            int size = 2000;
            do {
                queryFlowPurchaseListPageRequest.setSize(size);
                queryFlowPurchaseListPageRequest.setCurrent(current);
                page = flowSaleService.page(queryFlowPurchaseListPageRequest);
                if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                    break;
                }
                flowSaleDOList.addAll(page.getRecords());
                current++;
            } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        }
        Map<String, List<FlowSaleDO>> flowSaleDOMap = flowSaleDOList.stream().filter(e -> specIdList.contains(e.getSpecificationId())).collect(Collectors.groupingBy(e -> e.getEid() + "-" + e.getSpecificationId() + "-" + e.getSoBatchNo()));
        Map<String, BigDecimal> totalSoQuantityMap = new HashMap<>();
        for (String key : flowSaleDOMap.keySet()) {
            putMap(key, batchMap);
            List<FlowSaleDO> flowPurchaseList = flowSaleDOMap.get(key);
            BigDecimal totalSoQuantity = flowPurchaseList.stream().map(FlowSaleDO::getSoQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
            totalSoQuantityMap.put(key, totalSoQuantity);
        }
        // 加载当月的购进数据
        List<FlowPurchaseDO> flowPurchaseDOList = new ArrayList<>();
        {
            QueryFlowPurchaseListPageRequest queryFlowPurchaseListPageRequest = new QueryFlowPurchaseListPageRequest();
            queryFlowPurchaseListPageRequest.setStartTime(startTime);
            queryFlowPurchaseListPageRequest.setEndTime(endTime);
            queryFlowPurchaseListPageRequest.setSpecificationIdFlag(1);
            queryFlowPurchaseListPageRequest.setEidList(Arrays.asList(eid));

            Page<FlowPurchaseDO> page;
            int current = 1;
            int size = 2000;
            do {
                queryFlowPurchaseListPageRequest.setSize(size);
                queryFlowPurchaseListPageRequest.setCurrent(current);
                page = flowPurchaseService.page(queryFlowPurchaseListPageRequest);
                if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                    break;
                }
                flowPurchaseDOList.addAll(page.getRecords());
                current++;
            } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        }
        Map<String, List<FlowPurchaseDO>> flowPurchaseDOMap = flowPurchaseDOList.stream().filter(e -> specIdList.contains(e.getSpecificationId())).collect(Collectors.groupingBy(e -> e.getEid() + "-" + e.getSpecificationId() + "-" + e.getPoBatchNo()));
        Map<String, BigDecimal> totalPoQuantityMap = new HashMap<>();
        for (String key : flowPurchaseDOMap.keySet()) {
            putMap(key, batchMap);
            List<FlowPurchaseDO> flowPurchaseList = flowPurchaseDOMap.get(key);
            BigDecimal totalPoQuantity = flowPurchaseList.stream().map(FlowPurchaseDO::getPoQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
            totalPoQuantityMap.put(key, totalPoQuantity);
        }
        // 加载当月的最后一天库存数据
        List<FlowGoodsBatchDetailDO> flowGoodsBatchDetailDOList = new ArrayList<>();
        {
            QueryFlowGoodsBatchDetailListPageRequest queryFlowPurchaseListPageRequest = new QueryFlowGoodsBatchDetailListPageRequest();
            queryFlowPurchaseListPageRequest.setStartTime(startTime);
            queryFlowPurchaseListPageRequest.setEndTime(endTime);
            queryFlowPurchaseListPageRequest.setSpecificationIdFlag(1);
            queryFlowPurchaseListPageRequest.setEidList(Arrays.asList(eid));

            Page<FlowGoodsBatchDetailDO> page;
            int current = 1;
            int size = 2000;
            do {
                queryFlowPurchaseListPageRequest.setSize(size);
                queryFlowPurchaseListPageRequest.setCurrent(current);
                page = flowGoodsBatchDetailService.page(queryFlowPurchaseListPageRequest);
                if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                    break;
                }
                flowGoodsBatchDetailDOList.addAll(page.getRecords());
                current++;
            } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        }
        Map<String, List<FlowGoodsBatchDetailDO>> flowGoodsBatchDetailDOMap = flowGoodsBatchDetailDOList.stream().filter(e -> specIdList.contains(e.getSpecificationId())).collect(Collectors.groupingBy(e -> e.getEid() + "-" + e.getSpecificationId() + "-" + e.getGbBatchNo()));
        Map<String, BigDecimal> totalGbQuantityMap = new HashMap<>();
        for (String key : flowGoodsBatchDetailDOMap.keySet()) {
            putMap(key, batchMap);
            List<FlowGoodsBatchDetailDO> flowGoodsBatchDetailList = flowGoodsBatchDetailDOMap.get(key);
            BigDecimal totalGbQuantity = flowGoodsBatchDetailList.stream().map(FlowGoodsBatchDetailDO::getGbNumber).reduce(BigDecimal.ZERO, BigDecimal::add);
            totalGbQuantityMap.put(key, totalGbQuantity);
        }
        // 加载前一个月的最后一天库存数据
        List<FlowGoodsBatchDetailDO> flowGoodsBatchDetailLastDOList = new ArrayList<>();
        {
            Date lastDate = DateUtil.endOfMonth(DateUtil.offset(startTime, DateField.MONTH, -1));
            QueryFlowGoodsBatchDetailListPageRequest queryFlowPurchaseListPageRequest = new QueryFlowGoodsBatchDetailListPageRequest();
            queryFlowPurchaseListPageRequest.setStartTime(DateUtil.beginOfDay(lastDate));
            queryFlowPurchaseListPageRequest.setEndTime(DateUtil.endOfDay(lastDate));
            queryFlowPurchaseListPageRequest.setSpecificationIdFlag(1);
            queryFlowPurchaseListPageRequest.setEidList(Arrays.asList(eid));

            Page<FlowGoodsBatchDetailDO> page;
            int current = 1;
            int size = 2000;
            do {
                queryFlowPurchaseListPageRequest.setSize(size);
                queryFlowPurchaseListPageRequest.setCurrent(current);
                page = flowGoodsBatchDetailService.page(queryFlowPurchaseListPageRequest);
                if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                    break;
                }
                flowGoodsBatchDetailLastDOList.addAll(page.getRecords());
                current++;
            } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        }
        Map<String, List<FlowGoodsBatchDetailDO>> flowGoodsBatchDetailLastDOMap = flowGoodsBatchDetailLastDOList.stream().filter(e -> specIdList.contains(e.getSpecificationId())).collect(Collectors.groupingBy(e -> e.getEid() + "-" + e.getSpecificationId() + "-" + e.getGbBatchNo()));
        Map<String, BigDecimal> totalGbLastQuantityMap = new HashMap<>();
        for (String key : flowGoodsBatchDetailLastDOMap.keySet()) {
            putMap(key, batchMap);
            List<FlowGoodsBatchDetailDO> flowGoodsBatchDetailLastList = flowGoodsBatchDetailLastDOMap.get(key);
            BigDecimal totalGbLastQuantity = flowGoodsBatchDetailLastList.stream().map(FlowGoodsBatchDetailDO::getGbNumber).reduce(BigDecimal.ZERO, BigDecimal::add);
            totalGbLastQuantityMap.put(key, totalGbLastQuantity);
        }

        // 按照商业公司商品信息进行组装计算
        List<FlowPurchaseSalesInventoryDO> flowExcelList = new ArrayList<>();
        {
                EnterpriseDTO enterpriseDTO = enterpriseDTOMap.get(eid);
                if (enterpriseDTO != null) {
                    for (Long specId : specIdList) {
                        List<String> batchList = batchMap.get(specId);
                        if (CollUtil.isNotEmpty(batchList)) {
                            for (String batch : batchList) {
                                BigDecimal totalGbLastQuantity = totalGbLastQuantityMap.getOrDefault(eid + "-" + specId + "-" + batch, BigDecimal.ZERO);
                                BigDecimal totalGbQuantity = totalGbQuantityMap.getOrDefault(eid + "-" + specId + "-" + batch, BigDecimal.ZERO);
                                BigDecimal totalPoQuantity = totalPoQuantityMap.getOrDefault(eid + "-" + specId + "-" + batch, BigDecimal.ZERO);
                                BigDecimal totalSoQuantity = totalSoQuantityMap.getOrDefault(eid + "-" + specId + "-" + batch, BigDecimal.ZERO);
                                if (totalGbLastQuantity.compareTo(BigDecimal.ZERO) == 0 && totalGbQuantity.compareTo(BigDecimal.ZERO) == 0 && totalPoQuantity.compareTo(BigDecimal.ZERO) == 0 && totalSoQuantity.compareTo(BigDecimal.ZERO) == 0) {
                                    continue;
                                }
                                FlowPurchaseSalesInventoryDO flowExcel = new FlowPurchaseSalesInventoryDO();
                                flowExcel.setYear(String.valueOf(DateUtil.year(startTime)));
                                flowExcel.setMonth(String.valueOf(DateUtil.month(startTime) + 1));
                                flowExcel.setEid(eid);
                                flowExcel.setEname(enterpriseDTO.getName());
                                flowExcel.setLastNumber(totalGbLastQuantity);
                                flowExcel.setPurchaseNumber(totalPoQuantity);
                                flowExcel.setSaleNumber(totalSoQuantity);
                                flowExcel.setNumber(totalGbQuantity);
                                flowExcel.setCalculationNumber(totalGbLastQuantity.add(totalPoQuantity).subtract(totalSoQuantity));
                                flowExcel.setDiffNumber(flowExcel.getCalculationNumber().subtract(totalGbQuantity));
                                flowExcel.setBatchNo(batch);
                                CrmGoodsInfoDTO crmGoodsInfoDTO = crmGoodsMap.get(specId);
                                if (crmGoodsInfoDTO != null) {
                                    flowExcel.setBreed(crmGoodsInfoDTO.getVarietyType());
                                    flowExcel.setCrmGoodsId(crmGoodsInfoDTO.getGoodsCode());
                                    flowExcel.setSxPrice(crmGoodsInfoDTO.getAssessmentPrice());
                                    flowExcel.setGoodsName(crmGoodsInfoDTO.getGoodsName());
                                    flowExcel.setLastNumberAmount(flowExcel.getSxPrice().multiply(flowExcel.getLastNumber()));
                                    flowExcel.setSaleNumberAmount(flowExcel.getSxPrice().multiply(flowExcel.getSaleNumber()));
                                    flowExcel.setPurchaseNumberAmount(flowExcel.getSxPrice().multiply(flowExcel.getPurchaseNumber()));
                                    flowExcel.setSaleNumberAmount(flowExcel.getSxPrice().multiply(flowExcel.getSaleNumber()));
                                    flowExcel.setNumberAmount(flowExcel.getSxPrice().multiply(flowExcel.getNumber()));
                                    flowExcel.setCalculationNumberAmount(flowExcel.getSxPrice().multiply(flowExcel.getCalculationNumber()));
                                    flowExcel.setDiffNumberAmount(flowExcel.getSxPrice().multiply(flowExcel.getDiffNumber()));
                                } else {
                                    flowExcel.setBreed(String.valueOf(specId));
                                    flowExcel.setCrmGoodsId(specId);
                                    flowExcel.setGoodsName(String.valueOf(specId));
                                }
                                flowExcelList.add(flowExcel);
                            }
                        }
                    }
                }
            }
            this.saveOrUpdateBatch(flowExcelList);
        }
    }

    public void putMap(String key, Map<Long, List<String>> map) {
        String[] batchList = key.split("-");
        String batch = "";
        if (batchList.length > 2) {
            batch = batchList[2];
        }
        Long specId = Long.parseLong(key.split("-")[1]);
        List<String> list = null;
        if (map.containsKey(specId)) {
            list = map.get(specId);
            if (!list.contains(batch)) {
                list.add(batch);
            }
        } else {
            list = new ArrayList<>();
            list.add(batch);
            map.put(specId, list);
        }
    }
}
