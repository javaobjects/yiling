package com.yiling.pricing.goods.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询客户分组定价分页列表 request
 * @author yuecheng.chen
 * @date 2021-06-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGoodsPriceCustomerGroupPageListRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -4672345329942342009L;

	/**
	 * 企业ID
	 */
	private Long eid;

	/**
	 * 客户分组ID
	 */
	private Long customerGroupId;

	/**
	 * 商品ID集合
	 */
	private List<Long> goodsIds;

    /**
     * 产品线
     */
    private Integer goodsLine;
}
