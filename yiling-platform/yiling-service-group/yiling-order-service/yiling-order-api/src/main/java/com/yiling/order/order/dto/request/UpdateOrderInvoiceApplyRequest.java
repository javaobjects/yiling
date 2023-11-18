package com.yiling.order.order.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 提交发票申请，保存发票申请参数
 * @author:wei.wang
 * @date:2021/7/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateOrderInvoiceApplyRequest extends BaseRequest {
    /**
     * id
     */
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 订单金额
     */
    private BigDecimal totalAmount;

    /**
     * 现折金额
     */
    private BigDecimal cashDiscountAmount;


    /**
     * 转换规则编码
     */
    private String transitionRuleCode;

    /**
     * 是否使用票折：0-否 1-是
     */
    private Integer ticketDiscountFlag;

    /**
     * 票折单号
     */
    private String ticketDiscountNo;

    /**
     * 票折折扣金额
     */
    private BigDecimal ticketDiscountAmount;

    /**
     * 开票形式：1-整单开票 2-关联开票
     */
    private Integer invoiceForm;

    /**
     * 开票摘要
     */
    private String invoiceSummary;

    /**
     * 电子邮箱
     */
    private String invoiceEmail;


    /**
     * 发票金额
     */
    private BigDecimal invoiceAmount;


    /**
     * 状态：1-待申请 2-已申请 3-已开票 4-申请驳回 5-已作废
     */
    private Integer status;

    /**
     * ERP推送状态：1-未推送 2-推送成功 3-推送失败'
     */
    private Integer erpPushStatus;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;
}
