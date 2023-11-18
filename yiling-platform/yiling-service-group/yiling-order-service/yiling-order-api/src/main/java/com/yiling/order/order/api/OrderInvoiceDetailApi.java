package com.yiling.order.order.api;

import java.util.List;

import com.yiling.order.order.dto.OrderInvoiceDetailDTO;
import com.yiling.order.order.dto.request.SaveOrderInvoiceDetailRequest;

/**
 * 订单开票明细Api
 * @author:wei.wang
 * @date:2021/8/4
 */
public interface OrderInvoiceDetailApi {


    /**
     * 保存申请开票的时候提交申请明细信息
     * @param list
     * @return
     */
    Boolean saveBatchDate(List<SaveOrderInvoiceDetailRequest> list);

    /**
     * 根据订单ids获取开票申请明细信息
     * @param orders
     * @return
     */

    List<OrderInvoiceDetailDTO> listByOrderIds(List<Long> orders);

    /**
     * 根据组编号获取信息
     * @param groupNoList 组编号
     * @return
     */
    List<OrderInvoiceDetailDTO> listByGroupNoList(List<String> groupNoList);
}
