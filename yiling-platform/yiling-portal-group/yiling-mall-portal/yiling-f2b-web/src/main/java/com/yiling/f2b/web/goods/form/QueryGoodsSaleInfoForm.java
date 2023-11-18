package com.yiling.f2b.web.goods.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-06-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGoodsSaleInfoForm extends BaseForm {

	/**
	 * 以岭商品id
	 */
	@NotNull
	@ApiModelProperty(value = "以岭商品id")
	private Long goodsId;

	/**
	 * 配送商id
	 */
	@NotNull
	@ApiModelProperty(value = "配送商id")
	private Long distributorEid;

}
