package com.yiling.admin.data.center.goods.form;

import java.math.BigDecimal;

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
public class EditGoodsForm extends BaseForm {

	/**
	 * 商品库ID
	 */
    @NotNull
	@ApiModelProperty(value = "商品库ID", required = true)
	private Long goodsId;

	/**
	 * 库存数量
	 */
	@ApiModelProperty(value = "库存数量", example = "10")
	private Long qty;

	/**
	 * 商品状态：1上架 2下架 5待审核 6驳回
	 */
	@ApiModelProperty(value = "品状态：1上架 2下架", example = "1")
	private Integer goodsStatus;

	/**
	 * 挂网价
	 */
	@ApiModelProperty(value = "挂网价", example = "1.11")
	private BigDecimal price;
}
