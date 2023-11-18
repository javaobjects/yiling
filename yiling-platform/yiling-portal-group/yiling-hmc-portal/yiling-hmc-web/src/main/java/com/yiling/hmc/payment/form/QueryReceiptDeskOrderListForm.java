package com.yiling.hmc.payment.form;

import javax.validation.constraints.NotEmpty;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.payment.enums.PayChannelEnum;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询收营台订单列表 Form
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryReceiptDeskOrderListForm extends BaseForm {

    @NotEmpty
    @ApiModelProperty(value = "支付交易ID", required = true)
    private String payId;

    /**
     * 支付渠道{@link com.yiling.payment.enums.PayChannelEnum}
     */
    @ApiModelProperty(value = "支付渠道 yeePay(易宝支付),bocompay(交通银行支付)")
    private String payWay = PayChannelEnum.YEEPAY.getCode();
}
