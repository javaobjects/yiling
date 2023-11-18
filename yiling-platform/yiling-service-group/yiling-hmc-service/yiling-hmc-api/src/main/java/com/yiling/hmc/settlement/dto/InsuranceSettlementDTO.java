package com.yiling.hmc.settlement.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保司结账表
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsuranceSettlementDTO extends BaseDTO {

    /**
     * 客户姓名(被保人名称)
     */
    private String issueName;

    /**
     * 保险提供服务商id
     */
    private Long insuranceCompanyId;

    /**
     * 保险提供服务商名称
     */
    private String insuranceCompanyName;

    /**
     * 保单id
     */
    private Long insuranceRecordId;

    /**
     * 保险单号
     */
    private String policyNo;

    /**
     * 第三方打款单号
     */
    private String thirdPayNo;

    /**
     * 保司结算状态:1-待结算/2-已结算/3-无需结算失效单/4-预付款抵扣已结
     */
    private Integer insuranceSettleStatus;

    /**
     * 收款账号
     */
    private String accountNo;

    /**
     * 打款时间
     */
    private Date payTime;

    /**
     * 打款金额
     */
    private BigDecimal payAmount;

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

    /**
     * 备注
     */
    private String remark;


}
