package com.yiling.admin.data.center.goods.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-07-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "查询招标挂网价参数")
public class QueryGoodsBiddingPricePageListForm extends QueryPageListForm {

	/**
	 * 注册证号
	 */
	@ApiModelProperty(value = "注册证号", example = "Z109090")
	private String licenseNo;

	/**
	 * 生产厂家
	 */
	@ApiModelProperty(value = "生产厂家", example = "以岭")
	private String manufacturer;

	/**
	 * 商品名称
	 */
	@ApiModelProperty(value = "商品名称", example = "莲花")
	private String name;

}
