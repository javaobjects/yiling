package com.yiling.order.order.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/8/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderInvoicePullErpDTO extends BaseDTO {

    /**
     * 申请id
     */
    private Long applyId;

    /**
     * 转换规则编码
     */
    private String transitionRuleCode;

    /**
     * 邮箱
     */
    private String invoiceEmail;

    /**
     * 关联分组号
     */
    private String groupNo;

    /**
     * 关联的出库单号
     */
    private String groupDeliveryNos;

    /**
     * 开票摘要
     */
    private String invoiceSummary;

    /**
     * 卖家名称
     */
    private String sellerEname;
}
