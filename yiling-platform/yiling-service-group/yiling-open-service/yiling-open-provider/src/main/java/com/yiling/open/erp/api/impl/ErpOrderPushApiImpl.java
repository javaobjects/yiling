package com.yiling.open.erp.api.impl;

import java.util.List;

import com.yiling.open.erp.dto.ErpOrderDTO;
import com.yiling.open.erp.dto.ErpOrderPurchaseSendDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.open.erp.api.ErpOrderPushApi;
import com.yiling.open.erp.dto.ErpOrderPushDTO;
import com.yiling.open.erp.dto.request.OrderPushErpBuyerEidPageRequest;
import com.yiling.open.erp.dto.request.OrderPushErpPageRequest;
import com.yiling.open.erp.dto.request.UpdateErpOrderPushRequest;
import com.yiling.open.erp.service.ErpOrderPushService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2022/7/12
 */
@DubboService
@Slf4j
public class ErpOrderPushApiImpl implements ErpOrderPushApi {

    @Autowired
    private ErpOrderPushService erpOrderPushService;

    @Override
    public Page<ErpOrderPushDTO> getErpPushOrder(OrderPushErpPageRequest request) {
        return erpOrderPushService.getErpPushOrder(request);
    }

    @Override
    public Page<ErpOrderPushDTO> getErpPushBuyerOrder(OrderPushErpBuyerEidPageRequest request) {
        return erpOrderPushService.getErpPushBuyerOrder(request);
    }

    @Override
    public Boolean updateErpStatusNotPushToReadSuccess(List<UpdateErpOrderPushRequest> request) {
        return erpOrderPushService.updateErpStatusNotPushToReadSuccess(request);
    }

    @Override
    public Boolean updateExtractByOrderId(UpdateErpOrderPushRequest request) {
        return erpOrderPushService.updateExtractByOrderId(request);
    }

    @Override
    public Boolean updateExtractMessageByOrderId(UpdateErpOrderPushRequest request) {
        return erpOrderPushService.updateExtractMessageByOrderId(request);
    }

    @Override
    public List<ErpOrderPurchaseSendDTO> getOrderPurchaseSendBySuId(Long suId, Integer pageSize) {
        return erpOrderPushService.getOrderPurchaseSendBySuId(suId,pageSize);
    }

    @Override
    public List<ErpOrderPurchaseSendDTO> verifyERPOrderPurchaseSendResult(List<ErpOrderPurchaseSendDTO> erpOrderList) {
        return erpOrderPushService.verifyERPOrderPurchaseSendResult(erpOrderList);
    }

    @Override
    public List<ErpOrderDTO> getOrderSaleBySuId(Long suId, Integer pageSize) {
        return erpOrderPushService.getOrderSaleBySuId(suId,pageSize);
    }

    @Override
    public List<ErpOrderDTO> verifyERPOrderSaleResult(List<ErpOrderDTO> erpOrderList) {
        return erpOrderPushService.verifyERPOrderSaleResult(erpOrderList);
    }
}
