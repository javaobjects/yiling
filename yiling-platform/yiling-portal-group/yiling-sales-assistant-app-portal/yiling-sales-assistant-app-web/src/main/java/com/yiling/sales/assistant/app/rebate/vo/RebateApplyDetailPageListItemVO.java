package com.yiling.sales.assistant.app.rebate.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 返利申请明细表
 * </p>
 *
 * @author dexi.yao
 * @date 2021-09-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RebateApplyDetailPageListItemVO extends BaseVO {



	/**
	 * 明细类型 1-协议类型 2-其他
	 */
	@ApiModelProperty(value = "明细类型 1-协议类型 2-其他")
	private Integer detailType;

    /**
     * 协议id
     */
	@ApiModelProperty(value = "协议id")
    private Long agreementId;

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

}
