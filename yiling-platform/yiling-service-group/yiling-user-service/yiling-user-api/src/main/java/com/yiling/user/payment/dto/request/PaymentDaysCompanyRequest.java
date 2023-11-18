package com.yiling.user.payment.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 集团账期总额度
 * </p>
 *
 * @author tingwei.chen
 * @date 2021-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PaymentDaysCompanyRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 账期总额度
     */
    private BigDecimal totalAmount;

    /**
     * 已使用额度
     */
    private BigDecimal usedAmount;

    /**
     * 已还款额度
     */
    private BigDecimal repaymentAmount;


    /**
     * 集团可使用额度
     */
    private BigDecimal availableAmount;

}
