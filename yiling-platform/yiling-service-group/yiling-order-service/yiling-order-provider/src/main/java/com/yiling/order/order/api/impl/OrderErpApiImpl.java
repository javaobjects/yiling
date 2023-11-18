package com.yiling.order.order.api.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderErpApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderInvoicePullErpDTO;
import com.yiling.order.order.dto.request.CompleteErpOrderInvoiceRequest;
import com.yiling.order.order.dto.request.OrderPullErpDeliveryPageRequest;
import com.yiling.order.order.dto.request.OrderPullErpPageRequest;
import com.yiling.order.order.dto.request.UpdateErpOrderDeliveryRequest;
import com.yiling.order.order.dto.request.UpdateErpPushStatusRequest;
import com.yiling.order.order.entity.OrderDO;
import com.yiling.order.order.service.OrderErpService;

/**
 * ERP接口实现
 * @author:wei.wang
 * @date:2021/8/3
 */
@DubboService
public class OrderErpApiImpl implements OrderErpApi {

    @Autowired
    private OrderErpService orderErpService;

    @Override
    public  Map<String, Object>  updateErpReceivableNoByDeliveryNo(String erpDeliveryNo, String erpReceivableNo) {
        return orderErpService.updateErpReceivableNoByDeliveryNo(erpDeliveryNo, erpReceivableNo);
    }

    @Override
    public Map<String, Object> removeErpReceivableNo(String erpReceivableNo) {
        return orderErpService.removeErpReceivableNo(erpReceivableNo);
    }



    @Override
    public Boolean checkErpPullOrder(Long orderId) {

        return orderErpService.checkErpPullOrder(orderId);
    }

    /**
     * 获取未同步推送EAS申请发票订单id
     * @param request
     * @return
     */
    @Override
    public Page<OrderInvoicePullErpDTO> getErpPullOrderInvoice(OrderPullErpPageRequest request) {
        return orderErpService.getErpPullOrderInvoice(request);
    }

    /**
     * 申请发票同步ERP推送状态
     *
     * @param request
     * @return
     */
    @Override
    public Boolean updateErpOrderInvoicePushStatus(List<UpdateErpPushStatusRequest> request) {
        return orderErpService.updateErpOrderInvoicePushStatus(request);
    }

    /**
     * 作废发票
     *
     * @param invoiceList       发票号
     * @return
     */
    @Override
    public OrderDTO cancelErpOrderInvoice(List<String> invoiceList) {
        OrderDO orderDO = orderErpService.cancelErpOrderInvoice(invoiceList);
        return PojoUtils.map(orderDO,OrderDTO.class);
    }

    /**
     * 开票成功
     * @param request
     * @return
     */
    @Override
    public Boolean completeErpOrderInvoice(List<CompleteErpOrderInvoiceRequest> request) {
        return  orderErpService.completeErpOrderInvoice(request);
    }

    /**
     * 全部开票成功修改状态
     * @param orderId 订单id
     * @param invoiceAmount 开票金额
     * @param ticketDiscountAmount 票折金额
     * @return
     */
    @Override
    public Boolean invoiceAllSuccess(Long orderId, BigDecimal invoiceAmount, BigDecimal ticketDiscountAmount) {
        return  orderErpService.invoiceAllSuccess(orderId,invoiceAmount,ticketDiscountAmount);
    }



    @Override
    public Boolean checkErpPullBuyerOrder(Long orderId) {
        return orderErpService.checkErpPullBuyerOrder(orderId);
    }

    @Override
    public Boolean checkErpSendBuyerOrder(Long orderId) {
        return orderErpService.checkErpSendBuyerOrder(orderId);
    }


    /**
     * ERP拉取未出库的订单
     *
     * @param request
     * @return
     */
    @Override
    public Page<OrderDTO> getErpPullDeliveryOrder(OrderPullErpDeliveryPageRequest request) {

        return orderErpService.getErpPullDeliveryOrder(request);
    }

    /**
     * ERP推送出库的订单，并修改状态为以推送
     *
     * @param request
     * @return
     */
    @Override
    public Boolean updateErpDeliveryOrderById(List<UpdateErpOrderDeliveryRequest> request) {
        return orderErpService.updateErpDeliveryOrderById(request);
    }

}
