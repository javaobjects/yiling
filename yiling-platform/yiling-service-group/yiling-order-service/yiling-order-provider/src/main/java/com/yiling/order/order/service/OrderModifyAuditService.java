package com.yiling.order.order.service;

import com.yiling.framework.common.pojo.Result;
import com.yiling.order.order.bo.OrderModifyAuditChangeBO;
import com.yiling.order.order.dto.request.ModifyOrderNotAuditRequest;
import com.yiling.order.order.dto.request.UpdateOrderNotAuditRequest;

/**
 * 订单反审核
 * @author zhigang.guo
 * @date: 2021/8/6
 */
public interface OrderModifyAuditService {

    /**
     * 订单反审核
     * @param updateOrderNotAuditRequest
     * @return
     */
    @Deprecated
    Result<OrderModifyAuditChangeBO> modifyOrderNotAudit(UpdateOrderNotAuditRequest updateOrderNotAuditRequest);

    /**
     * 订单反审核V2版本
     * @param modifyOrderNotAuditRequest
     * @return
     */
    Result<OrderModifyAuditChangeBO> modifyOrderNotAudit_v2(ModifyOrderNotAuditRequest modifyOrderNotAuditRequest);

    /**
     * 反审生成退货单数据
     * @param orderNo
     * @return
     */
    boolean insertReturnOrderForModifyAudit(String orderNo);
}
