package com.yiling.hmc.order.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.insurance.dto.request.ConfirmTookRequest;
import com.yiling.hmc.insurance.dto.request.SaveClaimInformationRequest;
import com.yiling.hmc.order.api.OrderApi;
import com.yiling.hmc.order.bo.OrderBO;
import com.yiling.hmc.order.dto.OrderClaimInformationDTO;
import com.yiling.hmc.order.dto.OrderDTO;
import com.yiling.hmc.order.dto.request.OrderDeliverRequest;
import com.yiling.hmc.order.dto.request.OrderPageRequest;
import com.yiling.hmc.order.dto.request.OrderPrescriptionSaveRequest;
import com.yiling.hmc.order.dto.request.OrderReceiptsSaveRequest;
import com.yiling.hmc.order.dto.request.OrderReceivedRequest;
import com.yiling.hmc.order.dto.request.QueryCashPageRequest;
import com.yiling.hmc.order.dto.request.SyncOrderPageRequest;
import com.yiling.hmc.order.entity.OrderDO;
import com.yiling.hmc.order.service.OrderService;
import com.yiling.hmc.wechat.dto.request.ConfirmOrderRequest;
import com.yiling.hmc.wechat.dto.request.OrderNotifyRequest;
import com.yiling.hmc.wechat.dto.request.OrderSubmitRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: yong.zhang
 * @date: 2022/3/25
 */
@Slf4j
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderApiImpl implements OrderApi {

    private final OrderService orderService;

    @Override
    public OrderDTO queryById(Long id) {
        return orderService.getByOrderId(id);

    }

    @Override
    public List<OrderDTO> listByIdList(List<Long> idList) {
        List<OrderDO> doList = orderService.listByIds(idList);
        return PojoUtils.map(doList, OrderDTO.class);
    }

    @Override
    public OrderDTO queryByOrderNo(String orderNo) {
        OrderDO orderDO = orderService.queryByOrderNo(orderNo);
        return PojoUtils.map(orderDO, OrderDTO.class);
    }

    @Override
    public Page<OrderDTO> pageList(OrderPageRequest request) {
        Page<OrderDO> doPage = orderService.pageList(request);
        return PojoUtils.map(doPage, OrderDTO.class);
    }

    @Override
    public Page<OrderDTO> syncPageList(SyncOrderPageRequest request) {
        Page<OrderDO> doPage = orderService.syncPageList(request);
        return PojoUtils.map(doPage, OrderDTO.class);
    }

    @Override
    public boolean received(OrderReceivedRequest request) {
        return orderService.received(request);
    }

    @Override
    public boolean deliver(OrderDeliverRequest request) {
        return orderService.deliver(request);
    }

    @Override
    public OrderDTO createOrder(OrderSubmitRequest request) {
        return orderService.createOrder(request);
    }

    @Override
    public Boolean hasOrder(Long insuranceRecordId) {
        return orderService.hasOrder(insuranceRecordId);
    }

    @Override
    public OrderDTO getProcessingOrder(Long currentUserId) {
        return orderService.getProcessingOrder(currentUserId);
    }

    @Override
    public OrderDTO getUnPayOrder(OrderSubmitRequest request) {
        return orderService.getUnPayOrder(request);
    }

    @Override
    public OrderDTO getUnPickUPOrder(OrderSubmitRequest request) {
        return orderService.getUnPickUPOrder(request);
    }

    @Override
    public boolean confirmOrder(ConfirmOrderRequest request) {
        return orderService.confirmOrder(request);
    }

    @Override
    public OrderDTO getByOrderNo(String orderNo) {
        return orderService.getByOrderNo(orderNo);
    }

    @Override
    public Long orderNotify(OrderNotifyRequest orderNotifyRequest) {
        return orderService.orderNotify(orderNotifyRequest);
    }

    @Override
    public Page<OrderBO> queryCashPage(QueryCashPageRequest request) {
        return orderService.queryCashPage(request);
    }

    @Override
    public boolean sendMedicineRemind() {
        return orderService.sendMedicineRemind();
    }

    @Override
    public boolean saveOrderReceipts(OrderReceiptsSaveRequest request) {
        return orderService.saveOrderReceipts(request);
    }

    @Override
    public boolean saveOrderPrescription(OrderPrescriptionSaveRequest request) {
        return orderService.saveOrderPrescription(request);
    }

    @Override
    public OrderClaimInformationDTO getClaimInformation(Long id) {
        return orderService.getClaimInformation(id);
    }

    @Override
    public boolean submitClaimInformation(SaveClaimInformationRequest request) {
        return orderService.submitClaimInformation(request);
    }

    @Override
    public boolean confirmTook(ConfirmTookRequest request) {
        return orderService.confirmTook(request);
    }
}
