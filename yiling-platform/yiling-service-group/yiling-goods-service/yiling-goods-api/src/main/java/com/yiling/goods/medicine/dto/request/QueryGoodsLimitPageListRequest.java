package com.yiling.goods.medicine.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-05-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGoodsLimitPageListRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -4672345329942342009L;

	/**
	 * 注册证号(右模糊搜索）
	 */
	private String licenseNo;

	/**
	 * 生产厂家(全模糊搜索）
	 */
	private String manufacturer;

	/**
	 * 商品名称(全模糊搜索）
	 */
	private String name;

    /**
     * 限价id
     */
    private Long customerEid;
}
