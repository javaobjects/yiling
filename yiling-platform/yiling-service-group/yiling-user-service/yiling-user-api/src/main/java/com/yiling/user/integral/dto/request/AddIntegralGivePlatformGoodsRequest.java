package com.yiling.user.integral.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单送积分-添加平台SKU Request
 *
 * @author: lun.yu
 * @date: 2023-01-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddIntegralGivePlatformGoodsRequest extends BaseRequest {

    /**
     * 发放规则ID
     */
    private Long giveRuleId;

    /**
     * 规格ID-添加时使用
     */
    private Long sellSpecificationsId;

    /**
     * 规格ID-添加当前页时使用
     */
    private List<Long> sellSpecificationsIdList;

    /**
     * 商品ID-精确搜索
     */
    private Long standardIdPage;

    /**
     * 规格ID-精确搜索
     */
    private Long sellSpecificationsIdPage;

    /**
     * 商品名称-模糊搜索
     */
    private String goodsNamePage;

    /**
     * 生产厂家-模糊搜索
     */
    private String manufacturerPage;

    /**
     * 以岭品 0-全部 1-是 2-否
     */
    private Integer isYiLing;
}
