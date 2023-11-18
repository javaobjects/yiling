package com.yiling.mall.agreement.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

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
public class CalculateRebateApplyDTO extends BaseDTO {

	/**
	 * 总金额
	 */
	private BigDecimal totalAmount;

	/**
	 * 协议列表
	 */
	private List<AgreementDetail> agreementDetailList;

	/**
	 * 订单明细列表
	 */
	private List<OrderDetail> orderDetailList;

	/**
	 * 订单列表
	 */
	private List<Order> orderList;

	@Data
	public static class AgreementDetail implements Serializable {

		/**
		 * 协议id
		 */
		private Long id;

		/**
		 * 协议名称
		 */
		private String name;

		/**
		 * 协议内容
		 */
		private String content;

		/**
		 * 订单数量
		 */
		private Integer orderCount;

		/**
		 * 返利金额
		 */
		private BigDecimal amount;

		/**
		 * 销售组织
		 */
		private String sellerName;
		/**
		 * 销售组织easCode
		 */
		private String sellerCode;

		/**
		 * 销售组织eid
		 */
		private Long  sellerEid;

	}

	@Data
	public static class Order implements Serializable {

		/**
		 * 销售主体Eid
		 */
		private Long eid;

		/**
		 * 采购方eid
		 */
		private Long secondEid;

		/**
		 * 协议Id
		 */
		private Long agreementId;

		/**
		 * 协议条件Id
		 */
		private Long agreementConditionId;

		/**
		 * 版本号
		 */
		private Integer version;

		/**
		 * EAS企业账号
		 */
		private String easAccount;

		/**
		 * 订单Id
		 */
		private Long orderId;

		/**
		 * 返利订单表Id
		 */
		private Long rebateOrderId;

		/**
		 * 订单编号
		 */
		private String orderCode;

		/**
		 * 单据类型 1- 订单 2-退货单 3-结算单
		 */
		private Integer type;

		/**
		 * 返利订单明细表id
		 */
		private String rebateOrderDetailId;

		/**
		 * 协议返还金额
		 */
		private BigDecimal discountAmount;

	}

	@Data
	public static class OrderDetail implements Serializable {

		/**
		 * 协议id
		 */
		private Long id;

		/**
		 * 协议名称
		 */
		private String name;

		/**
		 * 协议内容
		 */
		private String content;

		/**
		 * 协议条件Id
		 */
		private Long agreementConditionId;

		/**
		 * 版本号
		 */
		private Integer version;

		/**
		 * easCode
		 */
		private String easCode;

		/**
		 * 订单号
		 */
		private String orderCode;

		/**
		 * 订单id
		 */
		private String orderId;

		/**
		 * 商品id
		 */
		private Long goodsId;

		/**
		 * 商品名称
		 */
		private String goodsName;

		/**
		 * 商品内码
		 */
		private String innerCode;

		/**
		 * 商品erp编码
		 */
		private String erpCode;

		/**
		 * 成交数量
		 */
		private Integer goodsQuantity;

		/**
		 * 小计
		 */
		private BigDecimal price;

		/**
		 * 返利金额
		 */
		private BigDecimal discountAmount;

	}

}
