package com.yiling.order.order.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/3/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderReturnVerifyRequest extends OrderReturnApplyRequest {

    /**
     * 退货单id
     */
    private Long returnId;

    /**
     * 审核是否通过 0-通过 1-驳回
     */
    private Integer isSuccess;

    /**
     * 驳回原因
     */
    private String failReason;

    /**
     * 是否判断开发票字段(以前只有以岭需要判断，后面新增了工业直属的也需要判断)
     * 0不需要，1需要
     */
    private Integer isYiLingInvoice;
}
