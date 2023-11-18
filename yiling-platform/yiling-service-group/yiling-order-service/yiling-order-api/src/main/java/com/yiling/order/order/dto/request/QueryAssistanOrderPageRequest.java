package com.yiling.order.order.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.order.order.enums.PaymentStatusEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 销售助手订单查询
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAssistanOrderPageRequest extends QueryPageListRequest {
    /**
     * 企业ID
     */
    private Long customerEid;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 订单类型
     */
    private Integer orderType;


    /**
     * 支付方式：1-线下支付 2-账期 3-预付款
     */
    private Integer paymentMethod;


    /**
     * 开始下单时间
     */
    private Date startCreateTime;

    /**
     * 结束下单时间
     */
    private Date endCreateTime;

    /**
     * 订单状态
     * @see com.yiling.order.order.enums.SaOrderStatusEnum
     */
    private Integer orderStatus;

    /**
     * 支付状态
     */
    private PaymentStatusEnum paymentStatusEnum;

    /**
     * 客户确认状态1-未转发,2-待客户确认,3-已确认
     */
    private List<Integer> customerConfirmStatus;

    /**
     * 审核状态：1-未提交 2-待审核 3-审核通过 4-审核驳回
     */
    private Integer auditStatus;

    /**
     * 创建人
     */
    private List<Long> contacterIds;

}
