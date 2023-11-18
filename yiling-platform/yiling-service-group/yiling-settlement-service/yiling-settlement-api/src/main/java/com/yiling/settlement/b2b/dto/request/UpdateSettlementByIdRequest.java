package com.yiling.settlement.b2b.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-11-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateSettlementByIdRequest extends BaseRequest {

	/**
	 * id
	 */
	private  Long id;

	/**
	 * 商家eid
	 */
	private Long eid;

	/**
	 * 结算单号
	 */
	private String code;

	/**
	 * 结算金额
	 */
	private BigDecimal amount;

	/**
	 * 货款金额
	 */
	private BigDecimal goodsAmount;

	/**
	 * 退款金额
	 */
	private BigDecimal refundAmount;

	/**
	 * 促销金额
	 */
	private BigDecimal saleAmount;

	/**
	 * 订单数量
	 */
	private Integer orderCount;

	/**
	 * 收款账户id
	 */
	private Long bankReceiptId;

	/**
	 * 结算单类型 1-货款结算单 2-促销结算单 3-预售违约金结算单
	 */
	private Integer type;

	/**
	 * 结算状态 1-待结算 2-银行处理中 3-已结算 4-结算失败
	 */
	private Integer status;

	/**
	 * 支付时间
	 */
	private Date settlementTime;

	/**
	 * 付款通道：1-易宝
	 */
	private Integer paySource;

	/**
	 * 支付通道付款单号
	 */
	private String thirdPayNo;

	/**
	 * 企业付款单号
	 */
	private String payNo;

	/**
	 * 打款失败原因
	 */
	private String errMsg;

	/**
	 * 单据锁定状态 1-未锁定 2-已锁定
	 */
	private Integer lockStatus;

	/**
	 * 打款失败原因
	 */
	private String lockCause;

	/**
	 * 备注
	 */
	private String remark;
}
