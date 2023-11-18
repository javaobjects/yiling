package com.yiling.sales.assistant.app.rebate.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.user.agreement.enums.RebateOrderTypeEnum;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 返利申请订单明细
 * </p>
 *
 * @author dexi.yao
 * @date 2021-09-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RebateOrderPageListItemVO extends BaseVO {

	/**
	 * 订单编号
	 */
	@ApiModelProperty(value = "订单编号")
	private String orderCode;

	/**
	 * 单据类型 1- 订单 2-退货单 3-结算单
	 */
	@ApiModelProperty(value = "单据类型 1- 订单 2-退货单 3-结算单")
	private Integer type;

    /**
     * 返利金额
     */
	@ApiModelProperty(value = "返利金额")
    private BigDecimal discountAmount;

	public BigDecimal getDiscountAmount() {
		if (ObjectUtil.equal(type, RebateOrderTypeEnum.REFUND.getCode())){
			discountAmount=discountAmount.negate();
		}
		return discountAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
	}

}
