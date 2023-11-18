package com.yiling.hmc.settlement.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/5/13
 */
@Data
public class InsuranceSettlementPageBO implements Serializable {

    /**
     * 保司结账表id
     */
    private Long id;

    /**
     * 保险提供服务商id
     */
    private Long insuranceCompanyId;

    /**
     * 服务商名称
     */
    private String insuranceCompanyName;

    /**
     * 交易打款时间
     */
    private Date payTime;

    /**
     * 对应第三方打款流水号
     */
    private String thirdPayNo;

    /**
     * 数据创建时间
     */
    private Date createTime;

    /**
     * 打款金额
     */
    private BigDecimal payAmount;

    /**
     * 对应保单号
     */
    private Long insuranceRecordId;

    /**
     * 保单号
     */
    private String policyNo;
}
