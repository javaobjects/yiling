package com.yiling.dataflow.check;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.check.bo.FlowPurchaseSpecificationIdTotalQuantityBO;
import com.yiling.dataflow.check.dto.FlowPurchaseCheckTaskDTO;
import com.yiling.dataflow.check.entity.FlowPurchaseCheckTaskDO;
import com.yiling.dataflow.order.dto.FlowPurchaseDTO;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;
import com.yiling.dataflow.order.entity.FlowPurchaseDO;
import com.yiling.dataflow.order.service.FlowPurchaseService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.check.service.FlowPurchaseCheckService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2022/9/6
 */
@Slf4j
public class FlowPurchaseCheckServiceTest extends BaseTest {


    @Autowired
    private FlowPurchaseCheckService flowPurchaseCheckService;

    @Autowired
    private FlowPurchaseService flowPurchaseService;

    @Test
    public void flowPurchaseCheckTest() {
        flowPurchaseCheckService.flowPurchaseCheck();
    }

    @Test
    public void getFlowPurchaseCheckTaskDOTest(){
        Date startTime=DateUtil.parseDateTime("2022-07-01 00:00:00");
        Date endTime=DateUtil.parseDateTime("2022-07-31 23:59:59");
        // 添加采购的task任务
        QueryFlowPurchaseListPageRequest request = new QueryFlowPurchaseListPageRequest();
        request.setStartTime(startTime);
        request.setEndTime(endTime);
        request.setSpecificationIdFlag(1);
        request.setEidList(Arrays.asList(2892L));
        // 未匹配到供应商的采购不统计
        request.setSupplierIdFlag(1);

        Page<FlowPurchaseDO> page;
        int current = 1;
        int size = 500;
        Map<String, FlowPurchaseSpecificationIdTotalQuantityBO> boMap = new HashMap<>();
        do {
            request.setSize(size);
            request.setCurrent(current);
            page = flowPurchaseService.page(request);
            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            // 取供应商已对接的采购
            // 按照eid、po_time、specification_id、supplier_id，分组统计每天的采购总数量
            Map<String, List<FlowPurchaseDO>> flowPurchaseMap = page.getRecords().stream()
                    .filter(o -> ObjectUtil.isNotNull(o.getSpecificationId()) && 0 != o.getSpecificationId().intValue() && ObjectUtil.isNotNull(o.getPoTime()))
                    .collect(Collectors.groupingBy(o -> o.getEid()+ "_" + DateUtil.format(o.getPoTime(), "yyyy-MM-dd") + "_" + o.getSpecificationId() + "_" + o.getSupplierId()+"_"+o.getPoBatchNo()));
            for (String key : flowPurchaseMap.keySet()) {
                List<FlowPurchaseDO> flowPurchaseList = flowPurchaseMap.get(key);
                BigDecimal totalPoQuantity = flowPurchaseList.stream().map(FlowPurchaseDO::getPoQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
                FlowPurchaseSpecificationIdTotalQuantityBO bo = boMap.get(key);
                if(ObjectUtil.isNull(bo)){
                    FlowPurchaseDO flowPurchaseDTO = flowPurchaseList.get(0);
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
            List<FlowPurchaseCheckTaskDO> purchaseCheckList = new ArrayList<>();
            List<Long> supplierIds=new ArrayList<>();
            for (String taskKey : boMap.keySet()) {
                FlowPurchaseSpecificationIdTotalQuantityBO bo = boMap.get(taskKey);
                FlowPurchaseCheckTaskDO dto = new FlowPurchaseCheckTaskDO();
                dto.setEid(bo.getEid());
                dto.setPoTime(bo.getPoTime());
                dto.setSpecificationId(bo.getSpecificationId());
                dto.setPoBatchNo(bo.getPoBatchNo());
                dto.setEname(bo.getEname());
                dto.setSupplierId(bo.getSupplierId());
                dto.setTotalPoQuantity(bo.getTotalPoQuantity());
                dto.setTotalSoQuantity(BigDecimal.ZERO);
                supplierIds.add(bo.getSupplierId());
                purchaseCheckList.add(dto);
            }
            //统计采购单数据
            flowPurchaseCheckService.getFlowPurchaseCheckTaskDO(purchaseCheckList,supplierIds,startTime,endTime);
            System.out.println(JSONObject.toJSONString(purchaseCheckList));
        }
    }
}
