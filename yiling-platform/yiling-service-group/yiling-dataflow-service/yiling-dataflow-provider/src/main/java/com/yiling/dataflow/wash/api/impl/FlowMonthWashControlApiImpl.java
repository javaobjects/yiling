package com.yiling.dataflow.wash.api.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowMonthWashControlPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateFlowMonthWashControlRequest;
import com.yiling.dataflow.wash.dto.request.UpdateStageRequest;
import com.yiling.dataflow.wash.service.FlowMonthWashControlService;
import com.yiling.framework.common.util.PojoUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author fucheng.bai
 * @date 2023/3/3
 */
@DubboService
public class FlowMonthWashControlApiImpl implements FlowMonthWashControlApi {

    @Autowired
    private FlowMonthWashControlService flowMonthWashControlService;

    @Override
    public boolean saveOrUpdate(SaveOrUpdateFlowMonthWashControlRequest saveOrUpdateFlowMonthWashControlRequest) {
        return flowMonthWashControlService.saveOrUpdate(saveOrUpdateFlowMonthWashControlRequest);
    }


    @Override
    public FlowMonthWashControlDTO getById(Long id) {
        return PojoUtils.map(flowMonthWashControlService.getById(id), FlowMonthWashControlDTO.class);
    }

    @Override
    public Page<FlowMonthWashControlDTO> listPage(QueryFlowMonthWashControlPageRequest request) {
        return flowMonthWashControlService.listPage(request);
    }

    @Override
    public FlowMonthWashControlDTO getByYearAndMonth(Integer year, Integer month) {
        return flowMonthWashControlService.getByYearAndMonth(year, month);
    }

    @Override
    public FlowMonthWashControlDTO getWashStatus() {
        return flowMonthWashControlService.getWashStatus();
    }



    @Override
    public FlowMonthWashControlDTO getCurrentFlowMonthWashControl() {
        return flowMonthWashControlService.getCurrentFlowMonthWashControl();
    }

    @Override
    public FlowMonthWashControlDTO getUnlockStatus() {
        return flowMonthWashControlService.getUnlockStatus();
    }

    @Override
    public FlowMonthWashControlDTO getGbLockStatus() {
        return flowMonthWashControlService.getGbLockStatus();
    }

    @Override
    public FlowMonthWashControlDTO getGbUnlockStatus() {
        return flowMonthWashControlService.getGbUnlockStatus();
    }

    @Override
    public FlowMonthWashControlDTO getBasisStatus() {
        return flowMonthWashControlService.getBasisStatus();
    }

    @Override
    public boolean verifyStatus() {
        return flowMonthWashControlService.verifyStatus();
    }
}
