package com.yiling.hmc.settlement.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/5/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsuranceSettlementRequest extends BaseRequest {

    /**
     * 客户姓名
     */
    private String issueName;

    /**
     * 保单号
     */
    private String policyNo;

    /**
     * 打款时间
     */
    private Date payTime;

    /**
     * 打款金额
     */
    private BigDecimal payAmount;

    /**
     * 收款账号
     */
    private String accountNo;

    /**
     * 服务商ID
     */
    private Long insuranceCompanyId;

    /**
     * 对应第三方打款流水号
     */
    private String thirdPayNo;

    /**
     * 对应药品订单号
     */
    private String orderNoStr;
}
