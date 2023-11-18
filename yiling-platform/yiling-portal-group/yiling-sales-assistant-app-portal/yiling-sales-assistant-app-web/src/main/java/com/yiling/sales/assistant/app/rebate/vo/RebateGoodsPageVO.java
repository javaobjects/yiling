package com.yiling.sales.assistant.app.rebate.vo;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2021/9/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("返利申请单-商品明细VO")
public class RebateGoodsPageVO<T> extends Page<T> {


	/**
	 * 订单数量
	 */
	@ApiModelProperty(value = "订单编号")
	private Integer orderCode;

	/**
	 * 返利金额
	 */
	@ApiModelProperty(value = "返利金额")
	private BigDecimal discountAmount;
}
