package com.yiling.hmc.payment.form;

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
     * 支付方式
     */
    @ApiModelProperty(value = "支付方式例如：yeePay(易宝支付),bocompay(交通银行支付)", required = true)
    private String payWay = "yeePay";
    /**
     * 支付来源
     */
    @ApiModelProperty(value = "支付来源例如:yeePayBank(标准收银台支付、无卡支付),yeePayWechat(微信支付),yeePayAlipay(支付宝支付),bocomPayWechat(交通银行微信),bocomPayAlipay(交通银行支付宝)", required = true)
    @NotEmpty
    private String paySource;

     /**
     * 支付成功请求url地址
     */
    @ApiModelProperty(value = "支付成功请求url地址", required = false)
    private String redirectUrl;

}



