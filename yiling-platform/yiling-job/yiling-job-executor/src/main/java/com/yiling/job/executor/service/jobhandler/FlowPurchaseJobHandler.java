package com.yiling.job.executor.service.jobhandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.dataflow.check.api.FlowPurchaseCheckApi;
import com.yiling.dataflow.check.api.FlowPurchaseCheckJobApi;
import com.yiling.dataflow.check.bo.FlowPurchaseSpecificationIdTotalQuantityBO;
import com.yiling.dataflow.check.dto.FlowPurchaseCheckJobDTO;
import com.yiling.dataflow.check.dto.FlowPurchaseCheckTaskDTO;
import com.yiling.dataflow.check.dto.request.FlowPurchaseCheckJobRequest;
import com.yiling.dataflow.order.api.FlowPurchaseApi;
import com.yiling.dataflow.order.dto.FlowPurchaseDTO;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;
import com.yiling.job.executor.log.JobLog;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.request.ErpClientQueryRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2022/9/6
 */
@Component
@Slf4j
public class FlowPurchaseJobHandler {

    @DubboReference(timeout = 10 * 1000)
    private FlowPurchaseCheckJobApi flowPurchaseCheckJobApi;
    @DubboReference(timeout = 5 * 1000)
    private ErpClientApi erpClientApi;
    @DubboReference(timeout = 5 * 1000)
    private EnterpriseApi enterpriseApi;
    @DubboReference(async = true)
    private FlowPurchaseCheckApi flowPurchaseCheckApi;
    @DubboReference(timeout = 60 * 1000)
    private FlowPurchaseApi flowPurchaseApi;

    public static final int SUB_SIZE = 500;


    @JobLog
    @XxlJob("flowPurchaseCheckJobHandler")
    public ReturnT<String> flowPurchaseCheckJobHandler(String param) throws Exception {
        log.info("[采购流向是否有对应的销售数据核查job], 调度任务开始");
        long start = System.currentTimeMillis();

        //判断任务是否已经生成
        Date date = DateUtil.parseDate(DateUtil.today());
        FlowPurchaseCheckJobRequest flowPurchaseCheckJobRequest=new FlowPurchaseCheckJobRequest();
        flowPurchaseCheckJobRequest.setTaskTime(date);
        List<FlowPurchaseCheckJobDTO> flowPurchaseCheckJobDTOList = flowPurchaseCheckJobApi.listByDate(flowPurchaseCheckJobRequest);
        if(CollUtil.isEmpty(flowPurchaseCheckJobDTOList)){
            // 调度任务时间，默认上个月初至月末
            DateTime lastMonth = DateUtil.lastMonth();
            DateTime startTime = DateUtil.beginOfMonth(lastMonth);
            DateTime endTime = DateUtil.endOfMonth(lastMonth);
            FlowPurchaseCheckJobDTO flowPurchaseCheckJobDTO = new FlowPurchaseCheckJobDTO();
            flowPurchaseCheckJobDTO.setTaskTime(date);
            flowPurchaseCheckJobDTO.setStartTime(startTime);
            flowPurchaseCheckJobDTO.setEndTime(endTime);
            FlowPurchaseCheckJobDTO checkJob = flowPurchaseCheckJobApi.save(flowPurchaseCheckJobDTO);

            // erp对接的商业公司
            List<Long> erpClientEidList = new ArrayList<>();
            getClientEidList(erpClientEidList);

            if(CollUtil.isEmpty(erpClientEidList)){
                log.info("[采购流向是否有对应的销售数据核查job], erpClientEidList isEmpty");
                return ReturnT.SUCCESS;
            }

            // 检查采购的商业公司，渠道类型等于二级商
            List<Long> eidList1 = new ArrayList<>();
            List<Long> eidList2 = new ArrayList<>();
            getEidListByEnterpriseChannel1(erpClientEidList, eidList1);
            getEidListByEnterpriseChannel2(erpClientEidList, eidList2);
            List<Long> eidList = new ArrayList<>();
            eidList.addAll(eidList1);
            eidList.addAll(eidList2);
            if(CollUtil.isEmpty(eidList)){
                log.info("[采购流向是否有对应的销售数据核查job], eidList isEmpty");
                return ReturnT.SUCCESS;
            }

            // 添加采购的task任务
            QueryFlowPurchaseListPageRequest request = new QueryFlowPurchaseListPageRequest();
            request.setStartTime(startTime);
            request.setEndTime(endTime);
            request.setSpecificationIdFlag(1);
            request.setEidList(eidList);
            // 未匹配到供应商的采购不统计
            request.setSupplierIdFlag(1);

            Page<FlowPurchaseDTO> page;
            int current = 1;
            int size = SUB_SIZE;
            Map<String,FlowPurchaseSpecificationIdTotalQuantityBO> boMap = new HashMap<>();
            do {
                request.setSize(size);
                request.setCurrent(current);
                page = flowPurchaseApi.page(request);
                if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                    break;
                }
                // 取供应商已对接的采购
                // 按照eid、po_time、specification_id、supplier_id，分组统计每天的采购总数量
                Map<String, List<FlowPurchaseDTO>> flowPurchaseMap = page.getRecords().stream()
                        .filter(o -> ObjectUtil.isNotNull(o.getSpecificationId()) && 0 != o.getSpecificationId().intValue() && ObjectUtil.isNotNull(o.getPoTime()) && erpClientEidList.contains(o.getSupplierId()))
                        .collect(Collectors.groupingBy(o -> o.getEid() + "_" + DateUtil.format(o.getPoTime(), "yyyy-MM-dd") + "_" + o.getSpecificationId() + "_" + o.getSupplierId()+"_"+o.getPoBatchNo()));
                for (String key : flowPurchaseMap.keySet()) {
                    List<FlowPurchaseDTO> flowPurchaseList = flowPurchaseMap.get(key);
                    BigDecimal totalPoQuantity = flowPurchaseList.stream().map(FlowPurchaseDTO::getPoQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
                    FlowPurchaseSpecificationIdTotalQuantityBO bo = boMap.get(key);
                    if(ObjectUtil.isNull(bo)){
                        FlowPurchaseDTO flowPurchaseDTO = flowPurchaseList.get(0);
                        FlowPurchaseSpecificationIdTotalQuantityBO boTemp = new FlowPurchaseSpecificationIdTotalQuantityBO();
                        boTemp.setEid(flowPurchaseDTO.getEid());
                        boTemp.setPoTime(DateUtil.parse(DateUtil.format(flowPurchaseDTO.getPoTime(), "yyyy-MM-dd"), "yyyy-MM-dd"));
                        boTemp.setSpecificationId(flowPurchaseDTO.getSpecificationId());
                        boTemp.setEname(flowPurchaseDTO.getEname());
                        boTemp.setSupplierId(flowPurchaseDTO.getSupplierId());
                        boTemp.setPoBatchNo(flowPurchaseDTO.getPoBatchNo());
                        boTemp.setTotalPoQuantity(totalPoQuantity);
                        boMap.put(key, boTemp);
                    } else {
                        BigDecimal totalPoQuantityOld = bo.getTotalPoQuantity();
                        BigDecimal totalPoQuantityNew = totalPoQuantityOld.add(totalPoQuantity);
                        bo.setTotalPoQuantity(totalPoQuantityNew);
                        boMap.put(key, bo);
                    }
                }

                if (page.getRecords().size() < size) {
                    break;
                }
                current++;
            } while (page != null && CollUtil.isNotEmpty(page.getRecords()));

            if(MapUtil.isNotEmpty(boMap)){
                List<FlowPurchaseCheckTaskDTO> purchaseCheckList = new ArrayList<>();
                for (String taskKey : boMap.keySet()) {
                    FlowPurchaseSpecificationIdTotalQuantityBO bo = boMap.get(taskKey);
                    FlowPurchaseCheckTaskDTO dto = new FlowPurchaseCheckTaskDTO();
                    dto.setCheckJobId(checkJob.getId());
                    dto.setEid(bo.getEid());
                    dto.setPoTime(bo.getPoTime());
                    dto.setSpecificationId(bo.getSpecificationId());
                    dto.setPoBatchNo(bo.getPoBatchNo());
                    dto.setEname(bo.getEname());
                    dto.setSupplierId(bo.getSupplierId());
                    dto.setTotalPoQuantity(bo.getTotalPoQuantity());
                    dto.setTotalSoQuantity(BigDecimal.ZERO);
                    purchaseCheckList.add(dto);
                }
                flowPurchaseCheckApi.saveBatch(purchaseCheckList);
            }
        }
        log.info("[采购流向是否有对应的销售数据核查job], 调度任务结束。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    private void getEidListByEnterpriseChannel2(List<Long> erpClientEidList, List<Long> eidList) {
        List<Long> enterpriseIdLevel2List = enterpriseApi.listEidsByChannel(EnterpriseChannelEnum.LEVEL_2);
        // 因连锁的销售是记录的终端公司名称，不统计连锁，只统计二级商
        if(CollUtil.isEmpty(enterpriseIdLevel2List)){
            return;
        }
        for (Long eid : erpClientEidList) {
            if(enterpriseIdLevel2List.contains(eid)){
                eidList.add(eid);
            }
        }
    }

    private void getEidListByEnterpriseChannel1(List<Long> erpClientEidList, List<Long> eidList) {
        List<Long> enterpriseIdLevel1List = enterpriseApi.listEidsByChannel(EnterpriseChannelEnum.LEVEL_1);
        // 因连锁的销售是记录的终端公司名称，不统计连锁，只统计二级商
        if(CollUtil.isEmpty(enterpriseIdLevel1List)){
            return;
        }
        for (Long eid : erpClientEidList) {
            if(enterpriseIdLevel1List.contains(eid)){
                eidList.add(eid);
            }
        }
    }

    private void getClientEidList(List<Long> erpClientEidList) {
        ErpClientQueryRequest clientRequest = new ErpClientQueryRequest();
        clientRequest.setClientStatus(1);
        clientRequest.setSyncStatus(1);
        clientRequest.setFlowStatus(1);
        Page<ErpClientDTO> erpClientPage = null;
        int erpClientCurrent = 1;
        int erpClientSize = SUB_SIZE;
        do {
            clientRequest.setCurrent(erpClientCurrent);
            clientRequest.setSize(erpClientSize);
            erpClientPage = erpClientApi.page(clientRequest);
            if (erpClientPage == null || CollUtil.isEmpty(erpClientPage.getRecords())) {
                break;
            }
            List<Long> rkSuIdList = erpClientPage.getRecords().stream().filter(o -> ObjectUtil.isNotNull(o.getRkSuId())).map(ErpClientDTO::getRkSuId).distinct().collect(Collectors.toList());
            if(CollUtil.isNotEmpty(rkSuIdList)){
                erpClientEidList.addAll(rkSuIdList);
            }
            if (erpClientPage.getRecords().size() < erpClientSize) {
                break;
            }
            erpClientCurrent ++;
        } while (erpClientPage != null && CollUtil.isNotEmpty(erpClientPage.getRecords()));
    }


    @JobLog
    @XxlJob("flowPurchaseCheckTaskHandler")
    public ReturnT<String> flowPurchaseCheckTaskHandler(String param) throws Exception {
        log.info("[采购流向是否有对应的销售数据核查task]，调度任务开始");
        long start = System.currentTimeMillis();
        flowPurchaseCheckApi.flowPurchaseCheck();
        log.info("[采购流向是否有对应的销售数据核查task]，调度任务结束。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

}
