package com.yiling.dataflow.wash.handler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.dto.FlowMonthInventoryDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowMonthPageRequest;
import com.yiling.dataflow.flowcollect.service.FlowMonthInventoryService;
import com.yiling.dataflow.order.entity.FlowGoodsBatchDetailDO;
import com.yiling.dataflow.order.service.FlowGoodsBatchDetailService;
import com.yiling.dataflow.utils.FlowDataIdUtils;
import com.yiling.dataflow.wash.entity.FlowGoodsBatchDetailWashDO;
import com.yiling.dataflow.wash.entity.FlowMonthWashControlDO;
import com.yiling.dataflow.wash.entity.FlowMonthWashTaskDO;
import com.yiling.dataflow.wash.entity.FlowSaleWashDO;
import com.yiling.dataflow.wash.enums.CollectionMethodEnum;
import com.yiling.dataflow.wash.enums.FlowDataWashStatusEnum;
import com.yiling.dataflow.wash.enums.WashMappingStatusEnum;
import com.yiling.dataflow.wash.service.FlowGoodsBatchDetailWashService;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/3/7
 */
@Slf4j
@Service
public class FlowGoodsBatchDetailWashHandler extends FlowWashHandler<FlowGoodsBatchDetailWashDO> {

    @Autowired
    private FlowMonthInventoryService flowMonthInventoryService;

    @Autowired
    private FlowGoodsBatchDetailService flowGoodsBatchDetailService;

    @Autowired
    private FlowGoodsBatchDetailWashService flowGoodsBatchDetailWashService;

    @Override
    protected void trimHandle(FlowGoodsBatchDetailWashDO entity) {
        if (StringUtils.isNotEmpty(entity.getGbName())) {
            entity.setGbName(entity.getGbName().trim());
        }
        if (StringUtils.isNotEmpty(entity.getGbSpecifications())) {
            entity.setGbSpecifications(entity.getGbSpecifications().trim());
        }
    }

    @Override
    protected boolean isNeedCheckFLowOut(FlowGoodsBatchDetailWashDO entity) {
        return false;
    }

    @Override
    protected boolean isNeedGoodsMatch() {
        return true;
    }

    @Override
    protected boolean isNeedCustomMatch() {
        return false;
    }

    @Override
    protected boolean isNeedSupplierMatch() {
        return false;
    }

    @Override
    protected boolean isNeedOrganizationCersIdMatch() {
        return false;
    }

    @Override
    public void setMappingStatus(FlowGoodsBatchDetailWashDO flowGoodsBatchDetailWashDO) {
        if (flowGoodsBatchDetailWashDO.getCrmGoodsCode() != null && flowGoodsBatchDetailWashDO.getCrmGoodsCode() > 0) {
            flowGoodsBatchDetailWashDO.setGoodsMappingStatus(1);
        }
    }

    @Override
    protected List<FlowGoodsBatchDetailWashDO> extractFlowDataList(FlowMonthWashTaskDO flowMonthWashTask, FlowMonthWashControlDO flowMonthWashControl) {
        List<FlowGoodsBatchDetailWashDO> list = new ArrayList<>();

        Integer collectionMethod = flowMonthWashTask.getCollectionMethod();
        if (CollectionMethodEnum.EXCEL.getCode().equals(collectionMethod)) {
            //  excel导入方式
            List<FlowMonthInventoryDTO> flowMonthInventoryDTOList = findFlowMonthInventoryByExcel(flowMonthWashTask.getId());
            for (FlowMonthInventoryDTO flowMonthInventoryDTO : flowMonthInventoryDTOList) {
                FlowGoodsBatchDetailWashDO flowGoodsBatchDetailWashDO = PojoUtils.map(flowMonthInventoryDTO, FlowGoodsBatchDetailWashDO.class);
                resetFlowGoodsBatchDetailWash(flowGoodsBatchDetailWashDO, flowMonthWashTask);
                flowGoodsBatchDetailWashDO.setGbDetailTime(DateUtil.parse(DateUtil.format(flowMonthWashControl.getDataEndTime(), "yyyy-MM-dd")));
                list.add(flowGoodsBatchDetailWashDO);
            }

        } else {
            //  获取区间日流向
            Long eid = flowMonthWashTask.getEid();
            Date gbDetailTime = DateUtil.parse(DateUtil.format(flowMonthWashControl.getDataEndTime(), "yyyy-MM-dd"));
            List<FlowGoodsBatchDetailDO> flowGoodsBatchDetailDOList = findDayFlowGoodsBatchDetail(eid, gbDetailTime);

            for (FlowGoodsBatchDetailDO flowGoodsBatchDetailDO : flowGoodsBatchDetailDOList) {
                FlowGoodsBatchDetailWashDO flowGoodsBatchDetailWashDO = PojoUtils.map(flowGoodsBatchDetailDO, FlowGoodsBatchDetailWashDO.class);
                resetFlowGoodsBatchDetailWash(flowGoodsBatchDetailWashDO, flowMonthWashTask);
                list.add(flowGoodsBatchDetailWashDO);
            }
        }
        flowGoodsBatchDetailWashService.batchInsert(list);

        return flowGoodsBatchDetailWashService.getByFmwtId(flowMonthWashTask.getId());
    }

    @Override
    protected void updateFlowWashEntity(FlowGoodsBatchDetailWashDO flowGoodsBatchDetailWashDO) {
        flowGoodsBatchDetailWashService.updateById(flowGoodsBatchDetailWashDO);
    }

    @Override
    protected void setConversionQuantity(FlowGoodsBatchDetailWashDO flowGoodsBatchDetailWashDO, Integer convertUnit, BigDecimal convertNumber) {
        try {
            // 设置换算数量
            BigDecimal conversionQuantity;
            if (convertNumber.compareTo(BigDecimal.ZERO) == 0) {
                conversionQuantity = flowGoodsBatchDetailWashDO.getGbNumber();
            } else {
                if (convertUnit == 1) {
                    conversionQuantity = flowGoodsBatchDetailWashDO.getGbNumber().multiply(convertNumber);
                } else {
                    conversionQuantity = flowGoodsBatchDetailWashDO.getGbNumber().divide(convertNumber, 6, RoundingMode.HALF_UP);
                }
            }
            flowGoodsBatchDetailWashDO.setConversionQuantity(conversionQuantity);
        } catch (Exception e) {
            log.error("库存月流向明细清洗，id={}，换算数量计算失败", flowGoodsBatchDetailWashDO.getId(), e);
        }

    }

    private List<FlowMonthInventoryDTO> findFlowMonthInventoryByExcel(Long fmwtId) {
        List<FlowMonthInventoryDTO> list = new ArrayList<>();
        QueryFlowMonthPageRequest request = new QueryFlowMonthPageRequest();
        request.setTaskId(fmwtId);

        int current = 1;
        int size = 1000;
        while (true) {
            request.setCurrent(current);
            request.setSize(size);
            Page<FlowMonthInventoryDTO> page = flowMonthInventoryService.queryFlowMonthInventoryPage(request);
            if (page.getRecords() == null || page.getRecords().size() == 0) {
                break;
            }
            list.addAll(page.getRecords());
            if (page.getRecords().size() < size) {
                break;
            }
            current++;
        }
        return list;
    }

    private List<FlowGoodsBatchDetailDO> findDayFlowGoodsBatchDetail(Long eid, Date gbDetailTime) {
        LambdaQueryWrapper<FlowGoodsBatchDetailDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowGoodsBatchDetailDO::getEid, eid);
        wrapper.eq(FlowGoodsBatchDetailDO::getGbDetailTime, gbDetailTime);

        return flowGoodsBatchDetailService.list(wrapper);
    }

    public void resetFlowGoodsBatchDetailWash(FlowGoodsBatchDetailWashDO flowGoodsBatchDetailWashDO, FlowMonthWashTaskDO flowMonthWashTask) {
        flowGoodsBatchDetailWashDO.setId(null);
        flowGoodsBatchDetailWashDO.setFlowKey(FlowDataIdUtils.nextId(flowMonthWashTask.getFlowClassify(), flowMonthWashTask.getFlowType()));
        flowGoodsBatchDetailWashDO.setCrmEnterpriseId(flowMonthWashTask.getCrmEnterpriseId());
        flowGoodsBatchDetailWashDO.setFmwtId(flowMonthWashTask.getId());
        flowGoodsBatchDetailWashDO.setYear(flowMonthWashTask.getYear());
        flowGoodsBatchDetailWashDO.setMonth(flowMonthWashTask.getMonth());
        flowGoodsBatchDetailWashDO.setName(flowMonthWashTask.getName());
        flowGoodsBatchDetailWashDO.setEid(flowMonthWashTask.getEid());

        flowGoodsBatchDetailWashDO.setCrmGoodsCode(0L);
        flowGoodsBatchDetailWashDO.setWashStatus(FlowDataWashStatusEnum.NOT_WASH.getCode());
        flowGoodsBatchDetailWashDO.setCreateUser(0L);
        flowGoodsBatchDetailWashDO.setUpdateUser(0L);
        flowGoodsBatchDetailWashDO.setCreateTime(new Date());
        flowGoodsBatchDetailWashDO.setUpdateTime(new Date());
        flowGoodsBatchDetailWashDO.setRemark("");
    }

}
