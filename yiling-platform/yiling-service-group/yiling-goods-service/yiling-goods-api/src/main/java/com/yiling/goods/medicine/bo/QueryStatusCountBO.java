package com.yiling.goods.medicine.bo;

import lombok.Data;

/**
 * @author dexi.yao
 * @date 2021-05-25
 */
@Data
public class QueryStatusCountBO implements java.io.Serializable {

    private static final long serialVersionUID = -7631547950698054406L;

	/**
	 * 商品状态：1上架 2下架 5待审核 6驳回
	 */
	private Integer goodsStatus;

	/**
	 * 对应数量
	 */
	private Long count;
}
