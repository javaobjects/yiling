package com.yiling.goods.medicine.bo;

import lombok.Data;

/**
 * @author dexi.yao
 * @date 2021-05-25
 */
@Data
public class QuerySellSpecificationsGoodsIdBO implements java.io.Serializable {

    private static final long serialVersionUID = -7631547950698054406L;

	/**
	 * 销售规格Id
	 */
	private Long sellSpecificationsId;

	/**
	 * 对应商品ID
	 */
	private Long goodsId;

    /**
     * 对应商品eid
     */
    private Long eid;
}
