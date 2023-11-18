package com.yiling.mall.order.api.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.mall.order.api.OrderInvoiceApplyProcessApi;
import com.yiling.mall.order.service.OrderInvoiceApplyProcessService;
import com.yiling.order.order.dto.request.SaveOrderTicketApplyInfoRequest;

/**
 * 开票信息处理
 * @author:wei.wang
 * @date:2021/7/26
 */

@DubboService
public class OrderInvoiceApplyProcessApiImpl  implements OrderInvoiceApplyProcessApi {
    @Autowired
    private OrderInvoiceApplyProcessService orderInvoiceApplyProcessService;

    /**
     * 申请开票
     *
     * @param request
     * @return
     */
    @Override
    public Boolean apply(SaveOrderTicketApplyInfoRequest request) {
        return orderInvoiceApplyProcessService.apply(request);
    }



    /**
     * 根据ERP出库单号更新ERP应收单号
     *
     * @param erpDeliveryNo   ERP出库单号
     * @param erpReceivableNo ERP应收单号
     * @return
     */
    @Override
    public Boolean updateErpReceivableNoByDeliveryNo(String erpDeliveryNo, String erpReceivableNo) {
        return orderInvoiceApplyProcessService.updateErpReceivableNoByDeliveryNo(erpDeliveryNo,erpReceivableNo);
    }

    /**
     * 作废应收单号
     *
     * @param erpReceivableNo 应收单号
     * @return
     */
    @Override
    public Boolean removeErpReceivableNo(String erpReceivableNo) {
        return orderInvoiceApplyProcessService.removeErpReceivableNo(erpReceivableNo);
    }


    /**
     * erp作废发票
     *
     * @param invoiceList 发票号
     * @return
     */
    @Override
    public Boolean cancelErpOrderInvoice(List<String> invoiceList) {
        return orderInvoiceApplyProcessService.cancelErpOrderInvoice(invoiceList);
    }


}
