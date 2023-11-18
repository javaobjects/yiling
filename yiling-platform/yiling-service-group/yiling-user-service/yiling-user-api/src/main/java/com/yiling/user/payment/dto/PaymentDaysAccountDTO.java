package com.yiling.user.payment.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 客户账期账户表
 * </p>
 *
 * @author gxl
 * @date 2021-05-21
 */
@Data
@Accessors(chain = true)
public class PaymentDaysAccountDTO extends BaseDTO {

    private static final long serialVersionUID = -1874940983299963004L;

    /**
     * 企业id（供应商）
     */
    private Long eid;
    /**
     * 企业名称
     */
    private String ename;

    /**
     * 客户id（采购商）
     */
    private Long customerEid;

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
     * 已使用额度
     */
    private BigDecimal usedAmount;


    /**
     * 历史已使用额度
     */
    private BigDecimal historyUseAmount;

    /**
     * 已还款额度
     */
    private BigDecimal repaymentAmount;

    /**
     * 还款时间
     */
    private Date repaymentTime;


    /**
     * 历史已还款额度
     */
    private BigDecimal historyRepaymentAmount;

    /**
     * 可用额度
     */
    private BigDecimal availableAmount;

    /**
     * 待还款金额
     */
    private BigDecimal needRepaymentAmount;

    /**
     * 类型：1-以岭 2-非以岭 3-工业直属
     */
    private Integer type;

    /**
     * 还款周期（天）
     */
    private Integer period;

    /**
     * 账期上浮点位（百分比）
     */
    private BigDecimal upPoint;

    /**
     * 是否长期有效：0-否 1-是
     */
    private Integer longEffective;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 结束时间（时间或长期有效）
     */
    private String endTimeName;

    /**
     * 状态：1-启用 2-停用
     */
    private Integer status;

    /**
     * 状态（导出时显示汉字）
     */
    private String statusName;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private Long updateUser;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

}
