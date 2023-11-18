package com.yiling.mall.order.service;

import com.yiling.framework.common.pojo.Result;
import com.yiling.order.order.dto.request.ModifyOrderNotAuditRequest;
import com.yiling.order.order.dto.request.UpdateOrderNotAuditRequest;

/**
 *  订单反审核
 * @author zhigang.guo
 * @date: 2021/8/9
 */
public interface OrderModifyAuditService {

    /**
     * 订单反审核
     * @param updateOrderNotAuditRequest
     * @return
     */
    @Deprecated
    public Result<Boolean> modifyOrderNotAudit(UpdateOrderNotAuditRequest updateOrderNotAuditRequest) ;


    /**
     * 订单反审V2版本
     * @param modifyOrderNotAuditRequest
     * @return
     */
    public Boolean modifyOrderNotAudit_v2(ModifyOrderNotAuditRequest modifyOrderNotAuditRequest);
}
