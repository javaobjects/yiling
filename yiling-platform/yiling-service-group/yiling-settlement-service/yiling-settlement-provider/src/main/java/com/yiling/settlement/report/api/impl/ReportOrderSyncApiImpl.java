package com.yiling.settlement.report.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.settlement.report.api.ReportOrderSyncApi;
import com.yiling.settlement.report.dto.ReportB2bOrderSyncDTO;
import com.yiling.settlement.report.dto.ReportFlowSaleOrderSyncDTO;
import com.yiling.settlement.report.dto.request.QueryFlowOrderPageListRequest;
import com.yiling.settlement.report.dto.request.QueryOrderSyncPageListRequest;
import com.yiling.settlement.report.dto.request.UpdateB2bOrderIdenRequest;
import com.yiling.settlement.report.dto.request.UpdateFlowOrderIdenRequest;
import com.yiling.settlement.report.dto.request.UpdateYlGoodsIdRequest;
import com.yiling.settlement.report.entity.B2bOrderSyncDO;
import com.yiling.settlement.report.entity.FlowSaleOrderSyncDO;
import com.yiling.settlement.report.service.B2bOrderSyncService;
import com.yiling.settlement.report.service.FlowSaleOrderSyncService;
import com.yiling.settlement.report.service.MemberSyncService;
import com.yiling.settlement.report.service.ReportSyncTaskService;

/**
 * @author: dexi.yao
 * @date: 2022-05-25
 */
@DubboService
public class ReportOrderSyncApiImpl implements ReportOrderSyncApi {

    @Autowired
    FlowSaleOrderSyncService flowSaleOrderSyncService;
    @Autowired
    MemberSyncService memberSyncService;
    @Autowired
    B2bOrderSyncService b2bOrderSyncService;
    @Autowired
    ReportSyncTaskService syncTaskService;

    @Override
    public Page<ReportB2bOrderSyncDTO> queryB2bOrderSyncInfoPageList(QueryOrderSyncPageListRequest request) {
        return b2bOrderSyncService.queryB2bOrderSyncInfoPageList(request);
    }

    @Override
    public Page<ReportFlowSaleOrderSyncDTO> queryFlowOrderPageList(QueryFlowOrderPageListRequest request) {
        return flowSaleOrderSyncService.queryFlowOrderPageList(request);
    }

    @Override
    public ReportFlowSaleOrderSyncDTO queryFlowOrderSyncById(Long id) {
        FlowSaleOrderSyncDO orderSyncDO = flowSaleOrderSyncService.getById(id);
        return PojoUtils.map(orderSyncDO, ReportFlowSaleOrderSyncDTO.class);
    }

    @Override
    public Boolean updateFlowOrderYlGoodsId(UpdateYlGoodsIdRequest request) {
        return flowSaleOrderSyncService.updateYlGoodsId(request);
    }

    @Override
    public List<ReportB2bOrderSyncDTO> queryB2bOrderReceiveInfo(Long orderId) {
        return b2bOrderSyncService.queryOrderSync(orderId);
    }

    @Override
    public ReportB2bOrderSyncDTO queryB2bOrderSyncById(Long id) {
        B2bOrderSyncDO orderSyncDO = b2bOrderSyncService.getById(id);
        return PojoUtils.map(orderSyncDO, ReportB2bOrderSyncDTO.class);
    }

    @Override
    public Boolean updateB2bOrderIdentification(UpdateB2bOrderIdenRequest request) {
        return b2bOrderSyncService.updateB2bOrderIdentification(request);
    }

    @Override
    public Boolean updateFlowOrderIdentification(UpdateFlowOrderIdenRequest request) {
        return flowSaleOrderSyncService.updateFlowOrderIdentification(request);
    }

    @Override
    public Long queryB2bOrderCount(QueryOrderSyncPageListRequest request) {
        return b2bOrderSyncService.queryB2bOrderNoPageList(request).getTotal();
    }

    @Override
    public Long queryFlowOrderCount(QueryFlowOrderPageListRequest request) {
        return flowSaleOrderSyncService.queryOrderCount(request);
    }

    @Override
    public void syncFlowOrder() {
        syncTaskService.syncFlowOrder();
    }

    @Override
    public Boolean initB2bOrderData() {
        return b2bOrderSyncService.initOrderData();
    }

    @Override
    public Boolean initMemberOrderData() {
        return memberSyncService.initMemberOrderData();
    }

    @Override
    public Boolean initFlowOrderData() {
        return flowSaleOrderSyncService.initFlowOrderData();
    }
}
