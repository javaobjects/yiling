package com.yiling.order.order.api;

import java.util.List;

import com.yiling.order.order.dto.OrderInvoiceDeliveryGroupDTO;
import com.yiling.order.order.dto.request.SaveOrderInvoiceDeliveryGroupRequest;

/**
 * 出库单关联开票分组信息
 * @author:wei.wang
 * @date:2021/10/8
 */
public interface OrderInvoiceDeliveryGroupApi {


    /**
     * 保存数据
     * @param request
     * @return
     */
    Boolean saveOne(SaveOrderInvoiceDeliveryGroupRequest request);

    /**
     * 根据申请信息获取
     * @param applyId
     * @return
     */
    List<OrderInvoiceDeliveryGroupDTO> listByApplyId(Long applyId);
}
