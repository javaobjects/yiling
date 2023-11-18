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
public class EditGoodsStatusForm extends BaseForm {

	/**
	 * 商品库ID
	 */
	@ApiModelProperty(value = "商品库ID", example = "1111")
	@NotNull(message = "商品库ID不能为空")
	private Long goodsId;

	/**
	 * 商品状态：1上架 2下架 5待审核 6驳回
	 */
	@ApiModelProperty(value = "品状态：1上架 2下架", example = "1")
	private Integer goodsStatus;

}
