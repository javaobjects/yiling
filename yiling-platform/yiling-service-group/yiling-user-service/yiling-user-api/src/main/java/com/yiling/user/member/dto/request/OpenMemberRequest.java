package com.yiling.user.member.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * <p>
 * B2B移动端-开通会员 Request
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/26
 */
@Data
@ApiModel
public class OpenMemberRequest extends BaseRequest {

    /**
     * 订单号
     */
    @NotEmpty
    private String orderNo;

    /**
     * 交易号
     */
    @NotEmpty
    private String tradeNo;

    /**
     * 订单状态：10-待支付 20-支付成功 30-支付失败
     */
    @NotNull
    private Integer status;

    /**
     * 第三方支付方式，如：yeePay
     */
    @NotEmpty
    private String payWay;

    /**
     * 支付渠道，如：yeePayAlipay
     */
    @NotEmpty
    private String payChannel;

}
