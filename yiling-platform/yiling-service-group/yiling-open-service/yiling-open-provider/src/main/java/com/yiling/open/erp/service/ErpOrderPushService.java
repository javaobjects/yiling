package com.yiling.open.erp.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.open.erp.dto.ErpOrderDTO;
import com.yiling.open.erp.dto.ErpOrderPurchaseSendDTO;
import com.yiling.open.erp.dto.ErpOrderPushDTO;
import com.yiling.open.erp.dto.request.OrderPushErpBuyerEidPageRequest;
import com.yiling.open.erp.dto.request.OrderPushErpPageRequest;
import com.yiling.open.erp.dto.request.UpdateErpOrderPushRequest;

/**
 * erp订单推送服务
 *
 * @Filename: ErpOrderPushService.java
 * @Version: 1.0
 * @Author: shuang.zhang 张爽
 */
public interface ErpOrderPushService {

    /**
     * 订单推送服务消费方法
     *
     * @param orderIds
     */
    void erpOrderPush(List<Long> orderIds);

    /**
     * 获取销售订单信息
     *
     * @param request
     * @return
     */
    Page<ErpOrderPushDTO> getErpPushOrder(OrderPushErpPageRequest request);

    /**
     * 获取采购订单信息
     *
     * @param request
     * @return
     */
    Page<ErpOrderPushDTO> getErpPushBuyerOrder(OrderPushErpBuyerEidPageRequest request);

    /**
     * ERP推送已同步订单，将为推送改为读取状态
     *
     * @param request
     * @return
     */
    Boolean updateErpStatusNotPushToReadSuccess(List<UpdateErpOrderPushRequest> request);

    /**
     * ERP推送已同步订单，并修改状态
     *
     * @param request
     * @return
     */
    Boolean updateExtractByOrderId(UpdateErpOrderPushRequest request);

    /**
     * ERP提取回写状态
     *
     * @param request
     * @return
     */
    Boolean updateExtractMessageByOrderId(UpdateErpOrderPushRequest request);

    /**
     * 订单id 查询订单推送状态
     *
     * @param orderIds
     * @return
     */
    List<ErpOrderPushDTO> getErpPushOrderListByOrderIds(List<Long> orderIds, Integer pushType);

    /**
     *  获取采购订单发货单信息
     * @param suId
     * @param pageSize
     */
    List<ErpOrderPurchaseSendDTO> getOrderPurchaseSendBySuId(Long suId, Integer pageSize);

    /**
     * 验证采购订单发货单信息
     * @param erpOrderList
     */
    List<ErpOrderPurchaseSendDTO> verifyERPOrderPurchaseSendResult(List<ErpOrderPurchaseSendDTO> erpOrderList);

    /**
     * 验证销售订单信息
     * @param erpOrderList
     */
    List<ErpOrderDTO> verifyERPOrderSaleResult(List<ErpOrderDTO> erpOrderList);

    /**
     * 获取销售订单
     * @param suId
     * @param pageSize
     * @return
     */
    List<ErpOrderDTO> getOrderSaleBySuId(Long suId, Integer pageSize);
}
