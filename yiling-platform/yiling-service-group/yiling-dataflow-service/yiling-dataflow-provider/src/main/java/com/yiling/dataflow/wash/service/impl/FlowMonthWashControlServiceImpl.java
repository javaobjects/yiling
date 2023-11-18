package com.yiling.dataflow.wash.service.impl;

import java.util.Arrays;
import java.util.List;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.backup.service.AgencyBackupService;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchDetailExistRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseExistRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowSaleExistRequest;
import com.yiling.dataflow.order.service.FlowGoodsBatchDetailService;
import com.yiling.dataflow.order.service.FlowPurchaseService;
import com.yiling.dataflow.order.service.FlowSaleService;
import com.yiling.dataflow.wash.bo.ErpClientSimpleBO;
import com.yiling.dataflow.wash.dao.FlowMonthWashControlMapper;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowMonthWashControlPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveFlowMonthWashTaskRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateFlowMonthWashControlRequest;
import com.yiling.dataflow.wash.dto.request.UpdateStageRequest;
import com.yiling.dataflow.wash.entity.FlowMonthWashControlDO;
import com.yiling.dataflow.wash.enums.FlowClassifyEnum;
import com.yiling.dataflow.wash.enums.FlowMonthWashControlMappingStatusEnum;
import com.yiling.dataflow.wash.enums.FlowMonthWashControlStatusEnum;
import com.yiling.dataflow.wash.enums.FlowTypeEnum;
import com.yiling.dataflow.wash.enums.WashErrorEnum;
import com.yiling.dataflow.wash.service.FlowMonthWashControlService;
import com.yiling.dataflow.wash.service.FlowMonthWashTaskService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fucheng.bai
 * @date 2023/3/3
 */
@Slf4j
@Service
public class FlowMonthWashControlServiceImpl extends BaseServiceImpl<FlowMonthWashControlMapper, FlowMonthWashControlDO> implements FlowMonthWashControlService {

    @Autowired
    private FlowMonthWashTaskService    flowMonthWashTaskService;
    @Autowired
    private CrmEnterpriseService        crmEnterpriseService;
    @Autowired
    private FlowPurchaseService         flowPurchaseService;
    @Autowired
    private FlowSaleService             flowSaleService;
    @Autowired
    private AgencyBackupService         agencyBackupService;
    @Autowired
    private FlowGoodsBatchDetailService flowGoodsBatchDetailService;
    @Autowired(required = false)
    private RocketMqProducerService     rocketMqProducerService;

    @Override
    public boolean saveOrUpdate(SaveOrUpdateFlowMonthWashControlRequest saveOrUpdateFlowMonthWashControlRequest) {
        return this.saveOrUpdate(PojoUtils.map(saveOrUpdateFlowMonthWashControlRequest, FlowMonthWashControlDO.class));
    }
//
//    @Override
//    public boolean updateStatusById(SaveOrUpdateFlowMonthWashControlRequest saveOrUpdateFlowMonthWashControlRequest) {
//        if (saveOrUpdateFlowMonthWashControlRequest.getId() == null || saveOrUpdateFlowMonthWashControlRequest.getId() == 0) {
//            return false;
//        }
//
//        if (saveOrUpdateFlowMonthWashControlRequest.getGoodsMappingStatus() != null && saveOrUpdateFlowMonthWashControlRequest.getGoodsMappingStatus().equals(FlowMonthWashControlMappingStatusEnum.MANUAL_OPEN.getCode())) {
//            FlowMonthWashControlDTO flowMonthWashControlDTO = getGoodsMappingStatus();
//            if (flowMonthWashControlDTO != null && !flowMonthWashControlDTO.getId().equals(saveOrUpdateFlowMonthWashControlRequest.getId())) {
//                throw new BusinessException(WashErrorEnum.FLOW_WASH_REPEAT_OPEN_ERROR);
//            }
//        }
//
//        if (saveOrUpdateFlowMonthWashControlRequest.getGoodsMappingStatus() != null && saveOrUpdateFlowMonthWashControlRequest.getGoodsMappingStatus().equals(FlowMonthWashControlMappingStatusEnum.MANUAL_CLOSE.getCode())) {
//            FlowMonthWashControlDTO flowMonthWashControlDTO = getGoodsMappingStatus();
//            if (flowMonthWashControlDTO != null && !flowMonthWashControlDTO.getId().equals(saveOrUpdateFlowMonthWashControlRequest.getId())) {
//                throw new BusinessException(WashErrorEnum.FLOW_WASH_REPEAT_OPEN_ERROR);
//            }
//        }
//
//        if (saveOrUpdateFlowMonthWashControlRequest.getCustomerMappingStatus() != null && saveOrUpdateFlowMonthWashControlRequest.getCustomerMappingStatus().equals(FlowMonthWashControlMappingStatusEnum.MANUAL_OPEN.getCode())) {
//            FlowMonthWashControlDTO flowMonthWashControlDTO = getCustomerMappingStatus();
//            if (flowMonthWashControlDTO != null && !flowMonthWashControlDTO.getId().equals(saveOrUpdateFlowMonthWashControlRequest.getId())) {
//                throw new BusinessException(WashErrorEnum.FLOW_WASH_REPEAT_OPEN_ERROR);
//            }
//        }
//
//        if (saveOrUpdateFlowMonthWashControlRequest.getGoodsBatchStatus() != null && saveOrUpdateFlowMonthWashControlRequest.getGoodsBatchStatus().equals(FlowMonthWashControlMappingStatusEnum.MANUAL_OPEN.getCode())) {
//            FlowMonthWashControlDTO flowMonthWashControlDTO = getGoodsBatchStatus();
//            if (flowMonthWashControlDTO != null && !flowMonthWashControlDTO.getId().equals(saveOrUpdateFlowMonthWashControlRequest.getId())) {
//                throw new BusinessException(WashErrorEnum.FLOW_WASH_REPEAT_OPEN_ERROR);
//            }
//        }
//
//        if (saveOrUpdateFlowMonthWashControlRequest.getFlowCrossStatus() != null && saveOrUpdateFlowMonthWashControlRequest.getFlowCrossStatus().equals(FlowMonthWashControlMappingStatusEnum.MANUAL_OPEN.getCode())) {
//            FlowMonthWashControlDTO flowMonthWashControlDTO = getFlowCrossStatus();
//            if (flowMonthWashControlDTO != null && !flowMonthWashControlDTO.getId().equals(saveOrUpdateFlowMonthWashControlRequest.getId())) {
//                throw new BusinessException(WashErrorEnum.FLOW_WASH_REPEAT_OPEN_ERROR);
//            }
//        }
//
//        if (CollUtil.isNotEmpty(saveOrUpdateFlowMonthWashControlRequest.getErpClientSimpleBOList()) && saveOrUpdateFlowMonthWashControlRequest.getGoodsMappingStatus().equals(FlowMonthWashControlMappingStatusEnum.MANUAL_CLOSE.getCode())) {
//            this.close(saveOrUpdateFlowMonthWashControlRequest.getErpClientSimpleBOList(), saveOrUpdateFlowMonthWashControlRequest.getId());
//        }
//        return this.updateById(PojoUtils.map(saveOrUpdateFlowMonthWashControlRequest, FlowMonthWashControlDO.class));
//    }
//
//    @Override
//    public boolean updateStageById(UpdateStageRequest request) {
//        return this.updateById(PojoUtils.map(request, FlowMonthWashControlDO.class));
//    }

    public boolean close(List<ErpClientSimpleBO> erpClientSimpleBOList, Long washControlId) {
        for (ErpClientSimpleBO erpClientSimpleBO : erpClientSimpleBOList) {
            flowGoodsBatchDetailService.executeGoodsBatchStatistics(erpClientSimpleBO.getEid(), DateUtil.parseDate(DateUtil.today()));
            FlowMonthWashControlDO flowMonthWashControlDO = this.getById(washControlId);
            SaveFlowMonthWashTaskRequest saveFlowMonthWashTaskRequest = new SaveFlowMonthWashTaskRequest();
            saveFlowMonthWashTaskRequest.setFmwcId(washControlId);
            saveFlowMonthWashTaskRequest.setCrmEnterpriseId(erpClientSimpleBO.getCrmEnterpriseId());
            saveFlowMonthWashTaskRequest.setEid(erpClientSimpleBO.getEid());
            saveFlowMonthWashTaskRequest.setCollectionMethod(erpClientSimpleBO.getFlowMode());
            saveFlowMonthWashTaskRequest.setFlowClassify(FlowClassifyEnum.NORMAL.getCode());

            QueryFlowPurchaseExistRequest queryFlowPurchaseExistRequest = new QueryFlowPurchaseExistRequest();
            queryFlowPurchaseExistRequest.setPoTimeStart(flowMonthWashControlDO.getDataStartTime());
            queryFlowPurchaseExistRequest.setPoTimeEnd(flowMonthWashControlDO.getDataEndTime());
            queryFlowPurchaseExistRequest.setEid(erpClientSimpleBO.getEid());
            boolean isHaveDataByEidAndPoTime = flowPurchaseService.isHaveDataByEidAndPoTime(queryFlowPurchaseExistRequest);
            if (isHaveDataByEidAndPoTime) {
                saveFlowMonthWashTaskRequest.setFlowType(FlowTypeEnum.PURCHASE.getCode());
                flowMonthWashTaskService.create(saveFlowMonthWashTaskRequest, true);
            }

            QueryFlowSaleExistRequest queryFlowSaleExistRequest = new QueryFlowSaleExistRequest();
            queryFlowSaleExistRequest.setSoTimeStart(flowMonthWashControlDO.getDataStartTime());
            queryFlowSaleExistRequest.setSoTimeEnd(flowMonthWashControlDO.getDataEndTime());
            queryFlowSaleExistRequest.setEid(erpClientSimpleBO.getEid());
            boolean isHaveDataByEidAndSoTime = flowSaleService.isHaveDataByEidAndSoTime(queryFlowSaleExistRequest);
            if (isHaveDataByEidAndSoTime) {
                saveFlowMonthWashTaskRequest.setFlowType(FlowTypeEnum.SALE.getCode());
                flowMonthWashTaskService.create(saveFlowMonthWashTaskRequest, true);
            }

            QueryFlowGoodsBatchDetailExistRequest queryFlowGoodsBatchDetailExistRequest = new QueryFlowGoodsBatchDetailExistRequest();
            queryFlowGoodsBatchDetailExistRequest.setEid(erpClientSimpleBO.getEid());
            queryFlowGoodsBatchDetailExistRequest.setGbDetailTime(DateUtil.parseDate(DateUtil.formatDate(flowMonthWashControlDO.getDataEndTime())));
            boolean isHaveDataByEidAndGbDetailTime = flowGoodsBatchDetailService.isHaveDataByEidAndGbDetailTime(queryFlowGoodsBatchDetailExistRequest);
            if (isHaveDataByEidAndGbDetailTime) {
                saveFlowMonthWashTaskRequest.setFlowType(FlowTypeEnum.GOODS_BATCH.getCode());
                flowMonthWashTaskService.create(saveFlowMonthWashTaskRequest, true);
            }
        }
        return true;
    }

    @Override
    public Page<FlowMonthWashControlDTO> listPage(QueryFlowMonthWashControlPageRequest request) {
        LambdaQueryWrapper<FlowMonthWashControlDO> wrapper = new LambdaQueryWrapper<>();
        if (request.getYear() != null && request.getYear() != 0) {
            wrapper.eq(FlowMonthWashControlDO::getYear, request.getYear());
        }
        if (request.getMonth() != null && request.getMonth() != 0) {
            wrapper.eq(FlowMonthWashControlDO::getMonth, request.getMonth());
        }
        wrapper.orderByDesc(FlowMonthWashControlDO::getId);
        Page<FlowMonthWashControlDO> page = new Page<>(request.getCurrent(), request.getSize());
        return PojoUtils.map(this.baseMapper.selectPage(page, wrapper), FlowMonthWashControlDTO.class);
    }

    @Override
    public FlowMonthWashControlDTO getByYearAndMonth(Integer year, Integer month) {
        LambdaQueryWrapper<FlowMonthWashControlDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowMonthWashControlDO::getYear, year);
        wrapper.eq(FlowMonthWashControlDO::getMonth, month);
        return PojoUtils.map(this.getOne(wrapper), FlowMonthWashControlDTO.class);
    }

    @Override
    public FlowMonthWashControlDTO getWashStatus() {
        QueryWrapper<FlowMonthWashControlDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(FlowMonthWashControlDO::getWashStatus,2);
        List<FlowMonthWashControlDO> flowMonthWashControlDOList = this.list(queryWrapper);
        if (CollUtil.isNotEmpty(flowMonthWashControlDOList)) {
            return PojoUtils.map(flowMonthWashControlDOList.get(0), FlowMonthWashControlDTO.class);
        }
        return null;
    }

    @Override
    public FlowMonthWashControlDTO getCurrentFlowMonthWashControl() {
        LambdaQueryWrapper<FlowMonthWashControlDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowMonthWashControlDO::getTaskStatus, 1);
        return PojoUtils.map(this.getOne(wrapper), FlowMonthWashControlDTO.class);
    }

    @Override
    public FlowMonthWashControlDTO getUnlockStatus() {
        LambdaQueryWrapper<FlowMonthWashControlDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowMonthWashControlDO::getUnlockStatus, 2);
        return PojoUtils.map(this.getOne(wrapper), FlowMonthWashControlDTO.class);
    }

    @Override
    public FlowMonthWashControlDTO getGbLockStatus() {
        LambdaQueryWrapper<FlowMonthWashControlDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowMonthWashControlDO::getGbLockStatus, 2);
        return PojoUtils.map(this.getOne(wrapper), FlowMonthWashControlDTO.class);
    }

    @Override
    public FlowMonthWashControlDTO getGbUnlockStatus() {
        LambdaQueryWrapper<FlowMonthWashControlDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowMonthWashControlDO::getGbUnlockStatus, 2);
        return PojoUtils.map(this.getOne(wrapper), FlowMonthWashControlDTO.class);
    }

    @Override
    public FlowMonthWashControlDTO getBasisStatus() {
        LambdaQueryWrapper<FlowMonthWashControlDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowMonthWashControlDO::getBasisStatus, 2);
        return PojoUtils.map(this.getOne(wrapper), FlowMonthWashControlDTO.class);
    }

    @Override
    public boolean verifyStatus() {
        QueryWrapper<FlowMonthWashControlDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().ne(FlowMonthWashControlDO::getTaskStatus, 2);
        int i = this.count(queryWrapper);
        return i > 0;
    }

}
