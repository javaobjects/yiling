package com.yiling.order.order.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderInvoicePullErpDTO;
import com.yiling.order.order.dto.request.CompleteErpOrderInvoiceRequest;
import com.yiling.order.order.dto.request.OrderPullErpDeliveryPageRequest;
import com.yiling.order.order.dto.request.OrderPullErpPageRequest;
import com.yiling.order.order.dto.request.UpdateErpOrderDeliveryRequest;
import com.yiling.order.order.dto.request.UpdateErpPushStatusRequest;

/**
 * 订单ERP对接接口
 * @author:wei.wang
 * @date:2021/8/3
 */
public interface OrderErpApi {

    /**
     * 根据ERP出库单号更新ERP应收单号
     *
     * @param erpDeliveryNo ERP出库单号
     * @param erpReceivableNo ERP应收单号
     * @return
     */
    Map<String, Object>  updateErpReceivableNoByDeliveryNo(String erpDeliveryNo, String erpReceivableNo);

    /**
     * 作废应收单号
     *
     * @param erpReceivableNo 应收单号
     * @return
     */
    Map<String, Object> removeErpReceivableNo(String erpReceivableNo);



    /**
     * 校验是否嫩推送
     * @param orderId
     * @return
     */
    Boolean checkErpPullOrder(Long orderId);

    /**
     * 获取未同步推送EAS申请发票订单id
     * @param request
     * @return
     */
    Page<OrderInvoicePullErpDTO> getErpPullOrderInvoice(OrderPullErpPageRequest request);

    /**
     * 申请发票同步ERP推送状态
     * @param request
     * @return
     */
    Boolean updateErpOrderInvoicePushStatus(List<UpdateErpPushStatusRequest> request);


    /**
     * erp作废发票
     * @param invoiceList 发票号
     * @return
     */
    OrderDTO cancelErpOrderInvoice(List<String> invoiceList);

    /**
     * erp开票成功,回写单号修改状态
     * @param request
     * @return
     */
    Boolean completeErpOrderInvoice(List<CompleteErpOrderInvoiceRequest> request);

    /**
     * 全部开票成功修改状态
     * @param orderId 订单id
     * @param invoiceAmount 开票金额
     * @param ticketDiscountAmount 票折金额
     * @return
     */
    Boolean invoiceAllSuccess(Long orderId, BigDecimal invoiceAmount, BigDecimal ticketDiscountAmount);



    /**
     *  ERP判断卖家推送顶单状态
     * @param orderId
     * @return
     */
    Boolean checkErpPullBuyerOrder(Long orderId);

    /**
     *  ERP判断卖家发货订单状态
     * @param orderId
     * @return
     */
    Boolean checkErpSendBuyerOrder(Long orderId);

    /**
     * 获取未推送出库单的订单
     * @param request
     * @return
     */
    Page<OrderDTO> getErpPullDeliveryOrder(OrderPullErpDeliveryPageRequest request);

    /**
     * ERP推送出库的订单，并修改状态为以推送
     * @param request
     * @return
     */
    Boolean updateErpDeliveryOrderById(List<UpdateErpOrderDeliveryRequest> request);
}
