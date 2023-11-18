package com.yiling.f2b.admin.agreement.vo;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.framework.common.base.BaseVO;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-07-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CalculateRebateApplyVO extends BaseVO {

	/**
	 * 总金额
	 */
	@ApiModelProperty(value = "总金额")
	private BigDecimal totalAmount;

	/**
	 * 协议数量
	 */
	@ApiModelProperty(value = "协议数量")
	private Integer agreementCount;

	/**
	 * 协议列表
	 */
	@ApiModelProperty(value = "协议列表")
	private List<AgreementDetail> agreementDetailList;

	/**
	 * 订单列表
	 */
	@ApiModelProperty(value = "订单列表")
	private List<OrderDetail> orderDetailList;

	@Data
	public static class AgreementDetail {

		/**
		 * 协议id
		 */
		@ApiModelProperty(value = "协议id")
		private Integer id;

		/**
		 * 协议id
		 */
		@ApiModelProperty(value = "明细类型 1-协议类型 2-其他")
		private Integer detailType=1;

		/**
		 * 协议名称
		 */
		@ApiModelProperty(value = "协议名称")
		private String name;

		/**
		 * 协议内容
		 */
		@ApiModelProperty(value = "协议内容")
		private String content;

		/**
		 * 订单数量
		 */
		@ApiModelProperty(value = "订单数量")
		private Integer orderCount;

		/**
		 * 返利金额
		 */
		@ApiModelProperty(value = "返利金额")
		private BigDecimal amount;

		/**
		 * 发货组织
		 */
		@ApiModelProperty(value = "发货组织")
		private String sellerName;

		/**
		 * 发货组织eid
		 */
		@ApiModelProperty(value = "发货组织eid", hidden = true)
		@JsonIgnore
		private Long sellerEid;

	}

	@Data
	public static class OrderDetail {

		/**
		 * 协议id
		 */
		@ApiModelProperty(value = "协议id")
		private Integer id;

		/**
		 * 协议名称
		 */
		@ApiModelProperty(value = "协议名称")
		private String name;

		/**
		 * 协议内容
		 */
		@ApiModelProperty(value = "协议内容")
		private String content;

		/**
		 * 订单号
		 */
		@ApiModelProperty(value = "订单号")
		private String orderCode;

		/**
		 * 订单id
		 */
		@ApiModelProperty(value = "订单id", hidden = true)
		@JsonIgnore
		private String orderId;

		/**
		 * 退款单id
		 */
		@ApiModelProperty(value = "退款单id", hidden = true)
		@JsonIgnore
		private String returnId;

		/**
		 * 商品编码
		 */
		@ApiModelProperty(value = "商品编码")
		private Long goodsId;

		/**
		 * 商品名称
		 */
		@ApiModelProperty(value = "商品名称")
		private String goodsName;

		/**
		 * 商品内码
		 */
		@ApiModelProperty(value = "商品内码")
		private String innerCode;

		/**
		 * 商品erp编码
		 */
		@ApiModelProperty(value = "商品erp编码")
		private String erpCode;

		/**
		 * 单据类型 1-订单 2-退货单
		 */
		@ApiModelProperty(value = "单据类型 1-订单 2-退货单")
		private Integer orderType;

		/**
		 * 成交数量
		 */
		@ApiModelProperty(value = "成交数量")
		private Integer goodsQuantity;

		/**
		 * 商品单价
		 */
		@ApiModelProperty(value = "商品单价")
		private BigDecimal price;

		/**
		 * 返利金额
		 */
		@ApiModelProperty(value = "返利金额")
		private BigDecimal discountAmount;

		public Integer getOrderType() {
			if (ObjectUtil.isNull(orderId)) {
				return 2;
			} else {
				return 1;
			}
		}
	}

	public Integer getAgreementCount() {
		return agreementDetailList.size();
	}

}
