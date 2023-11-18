package com.yiling.payment.web.receiptdesk.form;

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
    @ApiModelProperty(value = "支付方式例如：yeePay", required = true, hidden = true)
    private String payWay = "yeePay";
    /**
     * 支付来源
     */
    @ApiModelProperty(value = "支付来源例如:yeePayBank(标准收银台支付、无卡支付),yeePayWechat(微信支付),yeePayAlipay(支付宝支付)", required = true)
    @NotEmpty
    private String paySource;

    /**
     * 操作平台。POP-PC平台：POP-PC；POP-APP平台：POP-APP；B2B-APP平台：B2B-APP；B2B-PC平台：B2B-PC；销售助手-APP平台：SA-APP；系统：sys
     */
    @ApiModelProperty(value = "操作平台。POP-PC平台：POP-PC；POP-APP平台：POP-APP；B2B-APP平台：B2B-APP；B2B-PC平台：B2B-PC,销售助手-APP平台：SA-APP", required = true)
    @NotEmpty
    private String tradeSource;

    /**
     * 微信openId，用于聚合支付-微信下单
     */
    @ApiModelProperty(value = "微信openId，当paySource为yeePayWechat时必填")
    private String openId;


     /**
     * 支付成功请求url地址
     */
    @ApiModelProperty(value = "支付成功请求url地址", required = false)
    private String redirectUrl;

}



