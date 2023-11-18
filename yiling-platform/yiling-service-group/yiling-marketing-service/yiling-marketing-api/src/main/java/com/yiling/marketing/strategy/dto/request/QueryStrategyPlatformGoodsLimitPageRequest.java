package com.yiling.marketing.strategy.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/8/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryStrategyPlatformGoodsLimitPageRequest extends QueryPageListRequest {

    /**
     * 营销活动id
     */
    private Long marketingStrategyId;

    /**
     * 商品ID-精确搜索
     */
    private Long standardId;

    /**
     * 规格ID-精确搜索
     */
    private Long sellSpecificationsId;

    /**
     * 商品名称-模糊搜索
     */
    private String goodsName;

    /**
     * 生产厂家-模糊搜索
     */
    private String manufacturer;

    /**
     * 以岭品 0-全部 1-是 2-否
     */
    private Integer isYiLing;
}
