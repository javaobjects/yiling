package com.yiling.order.order.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.order.order.dto.OrderInvoiceApplyDTO;
import com.yiling.order.order.dto.OrderInvoicePullErpDTO;
import com.yiling.order.order.dto.request.OrderPullErpPageRequest;
import com.yiling.order.order.entity.OrderInvoiceApplyDO;

/**
 * <p>
 * 订单开票申请 服务类
 * </p>
 *
 * @author wei.wang
 * @date 2021-07-02
 */
public interface OrderInvoiceApplyService extends BaseService<OrderInvoiceApplyDO> {

    /**
     * 根据orderIds获取订单开票信息
     * @param orderIds
     * @return
     */
    List<OrderInvoiceApplyDTO> getOrderInvoiceApplyByList(List<Long> orderIds);



    /**
     * 根据orderId获取所有开票申请
     * @param orderId
     * @return
     */
    List<OrderInvoiceApplyDTO> listByOrderId(Long orderId);

    /**
     * 获取未同步推送EAS申请发票订单id
     * @param request
     * @return
     */
    Page<OrderInvoicePullErpDTO> getErpPullOrderInvoice(OrderPullErpPageRequest request);



}
