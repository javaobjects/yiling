package com.yiling.user.payment.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 账期临时额度列表专用dto
 */
@Data
@Accessors(chain = true)
public class ShortTimeQuotaDTO extends BaseDTO {

    private static final long serialVersionUID = -1874940983299963004L;
    /**
     * 供应商ID
     */
    private String eid;
    /**
     * 供应商名称
     */
    private String ename;
    /**
     * 客户ID（采购商）
     */
    private String customerEid;
    /**
     * 客户名称（采购商）
     */
    private String customerName;

    /**
     * 账期额度
     */
    private BigDecimal totalAmount;

    /**
     * 临时额度
     */
    private BigDecimal temporaryAmount;

    /**
     * 已使用额度 = 历史已使用额度 + 该企业所有订单的‘订单金额’的和
     */
    private BigDecimal usedAmount;

    /**
     * 已还款额度
     */
    private BigDecimal repaymentAmount;

    /**
     * 可用额度
     */
    private BigDecimal availableAmount;

    /**
     * 申请临时额度
     */
    private BigDecimal shortTimeQuota;
    /**
     * 同意后可使用额度
     */
    private BigDecimal afterAgreementTotalAmount;

    /**
     * 未还款额度
     */
    private BigDecimal unRepaymentAmount;

    /**
     * 其中到期未还款额度
     */
    private BigDecimal exprUnRepaymentAmount;

    /**
     * 状态：1-启用 2-停用
     */
    private Integer status;

    /**
     * 还款周期（天）
     */
    private Integer period;

    /**
     * 申请状态
     */
    private Integer auditStatus;

    /**
     * 申请状态名称（导出时需要使用）
     */
    private String auditStatusName;

    /**
     * 审核人ID（导出时需要使用）
     */
    private Long updateUserId;

    /**
     * 申请时间
     */
    private Date createTime;

    /**
     * 审核时间
     */
    private Date updateTime;

    /**
     * 审核人
     */
    private String updateUser;

}
