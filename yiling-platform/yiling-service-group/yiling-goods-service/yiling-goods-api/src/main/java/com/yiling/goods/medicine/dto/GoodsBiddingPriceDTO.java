package com.yiling.goods.medicine.dto;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 招标挂网价dto
 * @author dexi.yao
 * @date 2021-07-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsBiddingPriceDTO extends BaseDTO {

	/**
	 * 商品ID
	 */
	private Long goodsId;

	/**
	 * 地区编码
	 */
	private String locationCode;

	/**
	 * 地区名称
	 */
	private String locationName;

	/**
	 * 招标价格
	 */
	private BigDecimal price;

}
