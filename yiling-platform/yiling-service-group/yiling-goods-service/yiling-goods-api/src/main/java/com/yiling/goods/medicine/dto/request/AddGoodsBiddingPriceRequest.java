package com.yiling.goods.medicine.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-08-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddGoodsBiddingPriceRequest extends BaseRequest {

	/**
	 * id
	 */
	private Long id;

	/**
	 * 商品ID
	 */
	private Long goodsId;

	/**
	 * 地区编码
	 */
	private String locationCode;

	/**
	 * 地区名称
	 */
	private String locationName;

	/**
	 * 招标价格
	 */
	private BigDecimal price;

	/**
	 * 创建人
	 */
	private Long createUser;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改人
	 */
	private Long updateUser;

	/**
	 * 修改时间
	 */
	private Date updateTime;

	/**
	 * 备注
	 */
	private String remark;
}
