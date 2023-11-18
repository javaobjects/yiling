package com.yiling.admin.pop.goods.form;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-08-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EditBiddingPriceForm extends BaseForm {

	/**
	 * 商品库ID
	 */
	@ApiModelProperty(value = "商品库ID", example = "1111")
	@NotNull(message = "商品库ID不能为空")
	private Long goodsId;

	/**
	 * 商品库ID
	 */
	@ApiModelProperty(value = "地区编码", example = "310000")
	@NotNull(message = "地区编码不能为空")
	private String locationCode;

	/**
	 * 挂网价
	 */
	@ApiModelProperty(value = "挂网价", example = "1.11")
	private BigDecimal price;
}
