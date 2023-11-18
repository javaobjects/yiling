package com.yiling.order.order.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.order.order.dto.OrderInvoiceDTO;
import com.yiling.order.order.entity.OrderInvoiceDO;

/**
 * <p>
 * 订单发票 服务类
 * </p>
 *
 * @author wei.wang
 * @date 2021-07-02
 */
public interface OrderInvoiceService extends BaseService<OrderInvoiceDO> {

    /**
     * 根据发票id获取发票明细
     * @param applyId
     * @return
     */
    List<OrderInvoiceDTO> getOrderInvoiceByApplyId(Long applyId);

    /**
     * 根据发票id获取发票明细
     * @param applyIds
     * @return
     */
    List<OrderInvoiceDTO> getOrderInvoiceByApplyIdList(List<Long> applyIds);

    /**
     * 根据发票单号模糊查询出所有单号
     * @param invoiceNo 发票单号
     * @return
     */
    List<OrderInvoiceDTO> getOrderInvoiceLikeInvoiceNo(String invoiceNo);


}
