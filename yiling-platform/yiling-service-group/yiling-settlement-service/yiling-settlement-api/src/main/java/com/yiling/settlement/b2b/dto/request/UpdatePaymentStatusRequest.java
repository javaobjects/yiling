package com.yiling.settlement.b2b.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-11-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdatePaymentStatusRequest extends BaseRequest {

	/**
	 * 交易状态：1-待支付 2-打款成功 3-已撤销,4-打款失败 5-银行处理中
	 */
	private Integer paymentStatus;

    /**
     * 支付通道付款单号
     */
    private String thirdPayNo;

	/**
	 * 企业付款no
	 */
	private String payNo;

	/**
	 * 失败原因
	 */
	private String errMsg;


}
