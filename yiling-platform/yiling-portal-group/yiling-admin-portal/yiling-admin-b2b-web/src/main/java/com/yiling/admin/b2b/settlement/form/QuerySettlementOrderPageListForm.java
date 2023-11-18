package com.yiling.admin.b2b.settlement.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

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
public class QuerySettlementOrderPageListForm extends QueryPageListForm {

	/**
	 * 订单号
	 */
    @ApiModelProperty(value = "订单号")
	private String orderNo;

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
    @ApiModelProperty(value = "预售违约结算状态 1-待结算 2-银行处理中 3-已结算 4-结算失败")
    private Integer presaleDefaultStatus;

	/**
	 * 最小创建时间
	 */
    @ApiModelProperty(value = "最小创建时间")
	private Date minCreateTime;

	/**
	 * 最大创建时间
	 */
    @ApiModelProperty(value = "最大创建时间")
	private Date maxCreateTime;

}
