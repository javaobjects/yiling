package com.yiling.user.integral.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单送积分-平台SKU-已添加指定平台SKU分页列表查询 Form
 *
 * @author: lun.yu
 * @date: 2023-01-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryIntegralGivePlatformGoodsPageRequest extends QueryPageListRequest {

    /**
     * 发放规则ID
     */
    private Long giveRuleId;

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
