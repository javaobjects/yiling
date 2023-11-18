package com.yiling.order.order.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.order.order.dto.request
 * @date: 2021/12/14
 */
@Data
@Accessors(chain = true)
public class ModifyOrderNotAuditRequest extends BaseRequest {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * ERP订单号
     */
    private String  erpDeliveryNo;

    /**
     * 批次号
     */
    private String batchNo;


}
