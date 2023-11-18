package com.yiling.b2b.app.payment.form;

import javax.validation.constraints.NotEmpty;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 在线支付
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OnlinePayForm extends BaseForm {

	@NotEmpty
	@ApiModelProperty(value = "支付交易ID", required = true)
	private String payId;

	/**
	 * 微信openId，用于聚合支付-微信下单
	 */
	@ApiModelProperty(value = "微信openId，当paySource为yeePayWechat时必填", required = true)
	private String openId;

	/**
	 * 支付方式
	 */
	@ApiModelProperty(value = "支付方式例如：wxPay,aliPay", required = true, hidden = true)
	private String payWay = "yeePay";

	/**
	 * 支付来源
	 */
	@ApiModelProperty(value = "支付来源例如:yeePayBank,yeePayWechat,yeePayAlipay", required = true)
	@NotEmpty
	private String paySource;

}
