package com.yiling.hmc.settlement.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/3/31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsuranceSettlementCallbackRequest extends BaseRequest {

    /**
     * 对应第三方支付流水号
     */
    private String thirdPayNo;

    /**
     * 以岭订单编号
     */
    private String orderNo;

    /**
     * 打款金额
     */
    private BigDecimal payAmount;

    /**
     * 打款时间
     */
    private Date payTime;
}
