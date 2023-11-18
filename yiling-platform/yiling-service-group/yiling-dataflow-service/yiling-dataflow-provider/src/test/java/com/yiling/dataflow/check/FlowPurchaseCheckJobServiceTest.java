package com.yiling.dataflow.check;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.yiling.dataflow.check.dto.request.FlowPurchaseCheckJobRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.check.api.FlowPurchaseCheckApi;
import com.yiling.dataflow.check.api.FlowPurchaseCheckJobApi;
import com.yiling.dataflow.check.bo.FlowPurchaseSpecificationIdTotalQuantityBO;
import com.yiling.dataflow.check.dto.FlowPurchaseCheckJobDTO;
import com.yiling.dataflow.check.dto.FlowPurchaseCheckTaskDTO;
import com.yiling.dataflow.order.api.FlowPurchaseApi;
import com.yiling.dataflow.order.dto.FlowPurchaseDTO;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;

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
@Slf4j
public class FlowPurchaseCheckJobServiceTest extends BaseTest {


    @DubboReference(timeout = 10 * 1000)
    private FlowPurchaseCheckJobApi flowPurchaseCheckJobApi;
    @DubboReference(timeout = 60 * 1000)
    private FlowPurchaseApi flowPurchaseApi;
    @DubboReference(async = true)
    private FlowPurchaseCheckApi flowPurchaseCheckApi;

    public static final int SUB_SIZE = 500;


    @Test
    public void flowPurchaseCheckTest() {
        log.info("任务开始：采购流向是否有对应的销售数据核查任务时间，任务执行");
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

            // 添加采购的task任务
            QueryFlowPurchaseListPageRequest request = new QueryFlowPurchaseListPageRequest();
            request.setStartTime(startTime);
            request.setEndTime(endTime);
            request.setSpecificationIdFlag(1);

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
                // 按照eid、po_time、specification_id，分组统计每天的采购总数量
                Map<String, List<FlowPurchaseDTO>> flowPurchaseMap = page.getRecords().stream()
                        .filter(o -> ObjectUtil.isNotNull(o.getSpecificationId()) && 0 != o.getSpecificationId().intValue() && ObjectUtil.isNotNull(o.getPoTime()))
                        .collect(Collectors.groupingBy(o -> o.getEid() + "_" + DateUtil.format(o.getPoTime(), "yyyy-MM-dd") + "_" + o.getSpecificationId()));
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
                    dto.setTotalPoQuantity(bo.getTotalPoQuantity());
                    purchaseCheckList.add(dto);
                }
                flowPurchaseCheckApi.saveBatch(purchaseCheckList);
            }
        }
        log.info("任务结束：采购流向是否有对应的销售数据核查任务时间，任务执行。耗时：" + (System.currentTimeMillis() - start));
    }
}
