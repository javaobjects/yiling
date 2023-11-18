package com.yiling.pricing.goods.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询商品定价分页列表 request
 * @author yuecheng.chen
 * @date 2021-06-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGoodsPricePageListRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -4672345329942342009L;

	/**
	 * 企业ID
	 */
	private Long eid;

	/**
	 * 注册证号
	 */
	private String licenseNo;

	/**
	 * 商品名称
	 */
	private String name;

	/**
	 * 商品状态：1上架 2下架 5待审核 6驳回
	 */
	private Integer goodsStatus;

	/**
	 * 商品ID集合
	 */
	private List<Long> goodsIds;
}
