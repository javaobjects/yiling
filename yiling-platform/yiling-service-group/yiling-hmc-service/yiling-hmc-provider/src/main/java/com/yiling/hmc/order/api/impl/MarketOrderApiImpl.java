package com.yiling.hmc.order.api.impl;

import java.util.List;

import com.yiling.hmc.order.dto.request.*;
import com.yiling.hmc.wechat.dto.request.*;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.order.api.MarketOrderApi;
import com.yiling.hmc.order.dto.AdminMarketOrderDTO;
import com.yiling.hmc.order.dto.MarketOrderDTO;
import com.yiling.hmc.order.service.MarketOrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: fan.shen
 * @date: 2023-02-16
 */
@Slf4j
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MarketOrderApiImpl implements MarketOrderApi {

    private final MarketOrderService marketOrderService;

    @Override
    public MarketOrderDTO createOrder(MarketOrderSubmitRequest request) {
        return marketOrderService.createOrder(request);
    }

    @Override
    public MarketOrderDTO createPrescriptionOrder(PrescriptionOrderSubmitRequest request) {
        return marketOrderService.createPrescriptionOrder(request);
    }

    @Override
    public MarketOrderDTO queryById(Long orderId) {
        return marketOrderService.queryById(orderId);
    }

    @Override
    public List<MarketOrderDTO> queryByIdList(List<Long> orderIdList) {
        return marketOrderService.queryByIdList(orderIdList);
    }

    @Override
    public Boolean payNotify(MarketOrderPayNotifyRequest payNotifyRequest) {
        return marketOrderService.payNotify(payNotifyRequest);
    }

    @Override
    public Boolean diagnosisOrderPayNotify(HmcDiagnosisOrderPaySuccessNotifyRequest request) {
        return marketOrderService.diagnosisOrderPayNotify(request);
    }

    @Override
    public Boolean prescriptionOrderRefundNotify(MarketOrderRefundNotifyRequest refundNotifyRequest) {
        return marketOrderService.prescriptionOrderRefundNotify(refundNotifyRequest);
    }

    @Override
    public Page<MarketOrderDTO> queryMarketOrderPage(QueryMarketOrderPageRequest request) {
        return marketOrderService.queryMarketOrderPage(request);
    }

    @Override
    public Boolean updateMarketOrder(UpdateMarketOrderRequest request) {
        return marketOrderService.updateMarketOrder(request);
    }


    @Override
    public Page<AdminMarketOrderDTO> queryAdminMarketOrderPage(QueryAdminMarkerOrderPageRequest request) {
        return marketOrderService.queryAdminMarketOrderPage(request);
    }

    @Override
    public void sendMsg(Long orderId) {
        marketOrderService.sendMsg(orderId);
    }

    @Override
    public void saveRemark(MarketOrderSaveRequest request) {
        marketOrderService.saveRemark(request);
    }

    @Override
    public void orderDelivery(MarketOrderDeliveryRequest request) {
        marketOrderService.orderDelivery(request);
    }

    @Override
    public void prescriptionOrderDelivery(MarketPrescriptionOrderDeliveryRequest request) {
        marketOrderService.prescriptionOrderDelivery(request);
    }

    @Override
    public void marketOrderAutoReceive(Integer day) {
        marketOrderService.marketOrderAutoReceive(day);
    }

    @Override
    public void prescriptionOrderReceive(MarketPrescriptionOrderReceiveRequest request) {
        marketOrderService.prescriptionOrderReceive(request);
    }

    @Override
    public MarketOrderDTO queryPrescriptionOrderByIhOrderId(Integer ihOrderId) {
        return marketOrderService.queryPrescriptionOrderByIhOrderId(ihOrderId);
    }

    @Override
    public MarketOrderDTO queryPrescriptionOrderByPrescriptionId(Long prescriptionId) {
        return marketOrderService.queryPrescriptionOrderByPrescriptionId(prescriptionId);
    }
}
