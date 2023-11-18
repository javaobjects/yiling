package com.yiling.user.agreement.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/7/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementAccountDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 协议Id
     */
    private Long agreementId;

    /**
     * 协议条件ID
     */
    private Long agreementConditionId;

    /**
     * eas账号
     */
    private String easAccount;

    /**
     * 计算时间
     */
    private Date calculateTime;

    /**
     * 待返利金额
     */
    private BigDecimal rebateAmount;

    /**
     * 已兑付金额
     */
    private BigDecimal cashAmount;

    /**
     * 已完成的值，但是没有达到条件
     */
    private BigDecimal completedConditionValue;
}

