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
 * @date 2021-10-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SettlementPageListItemVO extends BaseVO {

	/**
	 * 结算单号
	 */
	@ApiModelProperty(value = "结算单号")
	private String code;

	/**
	 * 结算状态 1-待结算 2-银行处理中 3-已结算 4-结算失败
	 */
	@ApiModelProperty(value = "结算状态 1-待结算 2-银行处理中 3-已结算 4-结算失败")
	private Integer status;

	/**
	 * 供应商名称
	 */
	@ApiModelProperty(value = "供应商名称")
	private String ename;

	/**
	 * eid
	 */
	@ApiModelProperty(value = "eid",hidden = true)
	@JsonIgnore
	private Long eid;

	/**
	 * 结算单类型 1-货款结算单 2-促销结算单 3-预售违约金结算单
	 */
	@ApiModelProperty(value = "结算单类型 1-货款结算单 2-促销结算单 3-预售违约金结算单")
	private Integer type;

	/**
	 * 订单数量
	 */
	@ApiModelProperty(value = "订单数量")
	private Integer orderCount;

	/**
	 * 货款金额
	 */
	@ApiModelProperty(value = "货款金额")
	private BigDecimal goodsAmount;

	/**
	 * 货款退款金额
	 */
	@ApiModelProperty(value = "货款退款金额")
	private BigDecimal refundGoodsAmount;

	/**
	 * 促销金额
	 */
	@ApiModelProperty(value = "促销金额")
	private BigDecimal discountAmount;

	/**
	 * 促销退款金额
	 */
	@ApiModelProperty(value = "促销退款金额")
	private BigDecimal refundDiscountAmount;

    /**
     * 预售违约金额
     */
    @ApiModelProperty(value = "预售违约金额")
    private BigDecimal presaleDefaultAmount;

    /**
     * 预售违约退款金额
     */
    @ApiModelProperty(value = "预售违约退款金额")
    private BigDecimal refundPresaleDefaultAmount=new BigDecimal("0");

    /**
     * 结算金额
     */
    @ApiModelProperty(value = "结算金额")
    private BigDecimal amount;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	/**
	 * 打款失败原因
	 */
	@ApiModelProperty(value = "打款失败原因")
	private String errMsg;

	/**
	 * 结算时间
	 */
	@ApiModelProperty(value = "结算时间")
	private Date settlementTime;

	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人")
	private String updateUserName;

	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人", hidden = true)
	@JsonIgnore
	private Long updateUser;

	public BigDecimal getAmount() {
		return amount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getGoodsAmount() {
		return goodsAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

    public BigDecimal getRefundGoodsAmount() {
        return refundGoodsAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getDiscountAmount() {
		return discountAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

    public BigDecimal getRefundDiscountAmount() {
        return refundDiscountAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getPresaleDefaultAmount() {
        return presaleDefaultAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
