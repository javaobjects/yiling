package com.yiling.order.order.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 申请开票信息入参
 * @author:wei.wang
 * @date:2021/7/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrderTicketApplyInfoRequest extends BaseRequest {

    /**
     * 订单id
     */
    private Long id;

    /**
     * 选择票折单号
     */
    private List<String> ticketDiscountNoList;

    /**
     * 是否使用票折：0-否 1-是
     */
    private Integer ticketDiscountFlag;
    /**
     *转换规则编码
     */
    private String transitionRuleCode;

    /**
     * 开票形式：1-整单开票 2-关联开票
     */
    private Integer invoiceForm;

    /**
     * 电子发票邮箱
     */
    private String invoiceEmail;

    /**
     * 票折信息
     */
    private List<OrderDetailTicketDiscountRequest> ticketDiscount;

    /**
     * 出库单集合
     */
    private List<String> erpDeliveryNoList;

    /**
     * 开票摘要
     */
    private String invoiceSummary;

    /**
     * 客户id
     */
    private Long eid;

}
