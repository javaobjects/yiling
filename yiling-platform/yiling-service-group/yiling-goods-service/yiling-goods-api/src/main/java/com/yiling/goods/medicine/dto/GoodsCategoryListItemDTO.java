package com.yiling.goods.medicine.dto;

import com.yiling.framework.common.base.BaseDTO;

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
public class GoodsCategoryListItemDTO extends BaseDTO {

    private static final long serialVersionUID = -4672345329942342009L;

	/**
	 * 父类ID
	 */
	private Long parentId;

	/**
	 * 分类名称
	 */
	private String name;

	/**
	 * 分类排序
	 */
	private Integer sort;
}
