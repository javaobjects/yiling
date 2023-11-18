package com.yiling.mall.order.service;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.order.order.dto.request.SaveOrderTicketApplyInfoRequest;

/**
 * <p>
 * 订单 开票服务
 * </p>
 *
 * @author:wei.wang
 * @date:2021/7/16
 */
public interface OrderInvoiceApplyProcessService {

    /**
     * 申请开票
     * @param request
     * @return
     */
    Boolean apply(SaveOrderTicketApplyInfoRequest request);


    /**
     * 作废应收单号
     *
     * @param erpReceivableNo 应收单号
     * @return
     */
    Boolean removeErpReceivableNo(String erpReceivableNo);

    /**
     * 根据ERP出库单号更新ERP应收单号
     *
     * @param erpDeliveryNo ERP出库单号
     * @param erpReceivableNo ERP应收单号
     * @return
     */
    Boolean updateErpReceivableNoByDeliveryNo(String erpDeliveryNo, String erpReceivableNo);


    /**
     * erp作废发票
     * @param invoiceList 发票号
     * @return
     */
    Boolean cancelErpOrderInvoice(List<String> invoiceList);


}
