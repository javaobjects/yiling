package com.yiling.hmc.settlement.form;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
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
public class InsuranceSettlementCallbackForm extends BaseForm {

    @ApiModelProperty("对应第三方支付流水号")
    private String thirdPayNo;

    @ApiModelProperty("以岭订单编号")
    private String orderNo;

    @ApiModelProperty("打款金额")
    private BigDecimal payAmount;

    @ApiModelProperty("打款时间")
    private Date payTime;
}
