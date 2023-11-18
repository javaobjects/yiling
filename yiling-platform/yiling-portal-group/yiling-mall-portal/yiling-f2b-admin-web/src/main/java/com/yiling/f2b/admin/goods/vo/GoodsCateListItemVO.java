package com.yiling.f2b.admin.goods.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-05-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsCateListItemVO extends BaseVO {

	/**
	 * 父类ID
	 */
	@ApiModelProperty(value = "父类ID", example = "1")
	private Long gcParentId;

	/**
	 * 分类名称
	 */
	@ApiModelProperty(value = "分类名称", example = "1")
	private String gcName;

	/**
	 * 分类排序
	 */
	@ApiModelProperty(value = "分类排序", example = "1")
	private Integer gcOrder;
}
