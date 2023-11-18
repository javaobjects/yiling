package com.yiling.open.web.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-08-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RebatePageListItemVO extends BaseVO {

	/**
	 * 票折单号
	 */
	@ApiModelProperty(value = "票折单号")
	private String ticketDiscountNo;

	/**
	 * 票折金额
	 */
	@ApiModelProperty(value = "票折金额")
	private BigDecimal totalAmount;

	/**
	 * 票折已使用金额
	 */
	@ApiModelProperty(value = "票折已使用金额")
	private BigDecimal usedAmount;

	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人",hidden = true)
	@JsonIgnore
	private Long updateUser;

	/**
	 * 修改人工号
	 */
	@ApiModelProperty(value = "修改人工号")
	private String updateUserCode;

	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人")
	private String updateUserName;

	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间")
	private Date updateTime;

	/**
	 * 票折使用明细
	 */
	@ApiModelProperty(value = "票折使用明细")
	private List<Order>  detail;

	@Data
	public static class Order{
		/**
		 * 订单ID
		 */
		@ApiModelProperty(value = "订单ID")
		private Long orderId;

		/**
		 * 订单号
		 */
		@ApiModelProperty(value = "订单号")
		private String orderNo;

		/**
		 * 票折单号
		 */
		@ApiModelProperty(value = "票折单号")
		private String ticketDiscountNo;

		/**
		 * ERP出库单号
		 */
		@ApiModelProperty(value = "ERP出库单号")
		private String erpDeliveryNo;

		/**
		 * ERP应收单号
		 */
		@ApiModelProperty(value = "ERP应收单号")
		private String erpReceivableNo;
	}
}
