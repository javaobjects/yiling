package com.yiling.admin.b2b.settlement.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-10-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SettlementOrderVO extends BaseVO {

	/**
	 * 订单id
	 */
	@ApiModelProperty(value = "订单id")
	private Long orderId;

	/**
	 * 订单号
	 */
	@ApiModelProperty(value = "订单号")
	private String orderNo;

	/**
	 * 卖家企业id
	 */
	@ApiModelProperty(value = "卖家企业id",hidden = true)
	@JsonIgnore
	private Long sellerEid;

	/**
	 * 供应商
	 */
	@ApiModelProperty(value = "供应商")
	private String sellerName;

	/**
	 * 买家id
	 */
	@ApiModelProperty(value = "买家id",hidden = true)
	@JsonIgnore
	private Long buyerEid;

	/**
	 * 采购商
	 */
	@ApiModelProperty(value = "采购商")
	private String buyerName;

	/**
	 * 应结算货款金额
	 */
	@ApiModelProperty(value = "应结算货款金额")
	private BigDecimal goodsAmount;

	/**
	 * 应结算促销金额
	 */
	@ApiModelProperty(value = "应结算促销金额")
	private BigDecimal salesAmount;

    /**
     * 应结算预售违约金金额
     */
    @ApiModelProperty(value = "应结算预售违约金金额")
    private BigDecimal pdAmount;

	/**
	 * 结算总金额
	 */
	@ApiModelProperty(value = "结算总金额")
	private BigDecimal totalAmount;

	/**
	 * 货款结算状态 1-待结算 2-银行处理中 3-已结算 4-结算失败
	 */
	@ApiModelProperty(value = "货款结算状态 1-待结算 2-银行处理中 3-已结算 4-结算失败")
	private Integer goodsStatus;

	/**
	 * 促销结算状态 1-待结算 2-银行处理中 3-已结算 4-结算失败
	 */
	@ApiModelProperty(value = "促销结算状态 1-待结算 2-银行处理中 3-已结算 4-结算失败")
	private Integer saleStatus;

    /**
     * 预售违约结算状态 1-待结算 2-银行处理中 3-已结算 4-银行处理失败
     */
    @ApiModelProperty(value = "促销结算状态 1-待结算 2-银行处理中 3-已结算 4-结算失败")
    private Integer presaleDefaultStatus;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	public BigDecimal getGoodsAmount() {
		return goodsAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getSalesAmount() {
		return salesAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getTotalAmount() {
		return totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

    public BigDecimal getPdAmount() {
        return pdAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
