package com.yiling.goods.medicine.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author dexi.yao
 * @date 2021-07-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsBiddingPriceRequest extends BaseRequest {

	private static final long serialVersionUID = -7017522519602170570L;

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

}
