package com.yiling.f2b.admin.goods.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-05-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EditGoodsQtyForm extends BaseForm {

	/**
	 * 商品库ID
	 */
	@ApiModelProperty(value = "商品库ID", example = "1111")
	@NotNull(message = "商品库ID不能为空")
	private Long goodsId;

	/**
	 * 库存数量
	 */
	@ApiModelProperty(value = "库存数量", example = "10")
	private Integer qty;

}
