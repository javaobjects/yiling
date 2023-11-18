package com.yiling.order.order.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.order.order.api.OrderInvoiceApi;
import com.yiling.order.order.dto.OrderInvoiceDTO;
import com.yiling.order.order.service.OrderInvoiceService;

/**
 * 订单发票号信息Api
 * @author:wei.wang
 * @date:2021/7/2
 */
@DubboService
public class OrderInvoiceApiImpl implements OrderInvoiceApi {

    @Autowired
    private OrderInvoiceService orderInvoiceService;
    /**
     * 根据发票id获取发票明细
     *
     * @param applyId
     * @return
     */
    @Override
    public List<OrderInvoiceDTO> getOrderInvoiceByApplyId(Long applyId) {

        return  orderInvoiceService.getOrderInvoiceByApplyId(applyId);
    }

    /**
     * 根据发票ids获取发票明细
     *
     * @param applyIds
     * @return
     */
    @Override
    public List<OrderInvoiceDTO> getOrderInvoiceByApplyIdList(List<Long> applyIds) {
        return  orderInvoiceService.getOrderInvoiceByApplyIdList(applyIds);
    }


}
