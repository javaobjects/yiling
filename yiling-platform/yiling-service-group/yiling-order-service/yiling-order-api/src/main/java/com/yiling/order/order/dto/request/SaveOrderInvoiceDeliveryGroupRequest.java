package com.yiling.order.order.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;

/**
 * 保存出库单关联开票分组信息
 * @author:wei.wang
 * @date:2021/10/8
 */
@Data
public class SaveOrderInvoiceDeliveryGroupRequest extends BaseRequest {
    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 申请id
     */
    private Long applyId;

    /**
     * 关联分组号
     */
    private String groupNo;

    /**
     * 关联的出库单号（多个英文逗号分隔）
     */
    private String groupDeliveryNos;

    /**
     * 开票摘要
     */
    private String invoiceSummary;

}
