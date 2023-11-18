package com.yiling.f2b.admin.agreement.vo;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-07-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("协议待兑付订单明细")
public class RebateOrderDetailPageListVO<T> extends Page<T> {

	/**
	 * 协议名称
	 */
	@ApiModelProperty(value = "协议名称")
	private String name;

	/**
	 * 协议分类：1-年度协议 2-补充协议
	 */
	@ApiModelProperty(value = "协议分类：1-年度协议 2-补充协议")
	private Integer category;

	/**
	 * 订单总数量
	 */
	@ApiModelProperty(value = "订单总数量")
	private Integer orderCount;

	/**
	 * 可对账订单数量
	 */
	@ApiModelProperty(value = "可对账订单数量")
	private Integer passOrderCount;

	/**
	 * 不可对账订单数量
	 */
	@ApiModelProperty(value = "不可对账订单数量")
	private Integer unPassOrderCount;

	/**
	 * 退款单数量
	 */
	@ApiModelProperty(value = "退款单数量")
	private Integer orderRefundCount;

	/**
	 * 可对账订单退款数量
	 */
	@ApiModelProperty(value = "可对账订单退款数量")
	private Integer passOrderRefundCount;

	/**
	 * 不可对账订单退款数量
	 */
	@ApiModelProperty(value = "不可对账订单退款数量")
	private Integer    unPassOrderRefundCount;

	/**
	 * 已兑付金额
	 */
	@ApiModelProperty(value = "已兑付金额")
	private BigDecimal discountAmount;


	public Integer getOrderRefundCount() {
		return passOrderRefundCount+unPassOrderRefundCount;
	}

	public Integer getOrderCount() {
		return passOrderCount+unPassOrderCount;
	}

}
