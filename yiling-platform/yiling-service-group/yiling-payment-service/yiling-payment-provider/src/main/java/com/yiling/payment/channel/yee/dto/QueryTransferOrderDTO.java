package com.yiling.payment.channel.yee.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;

/**
 * @author dexi.yao
 * @date 2021-11-12
 */
@Data
public class QueryTransferOrderDTO extends BaseDTO {

	/**
	 * 企业付款订单号
	 */
	private String payNo;

	/**
	 * 支付公司返回的付款订单号
	 */
	private String thirdTradeNo;

	/**
	 * 付款金额
	 */
	private BigDecimal orderAmount;

	/**
	 * 到账金额
	 */
	private BigDecimal receiveAmount;

	/**
	 * 手续费
	 */
	private BigDecimal fee;

	/**
	 * 付款完成时间
	 */
	private Date finishTime;

	/**
	 * 1-待支付 2-交易成功 3-交易取消 4-付款失败,5-银行处理中
	 */
	private Integer tradeStatus;

	/**
	 * 失败原因
	 */
	private String errMsg;
}
