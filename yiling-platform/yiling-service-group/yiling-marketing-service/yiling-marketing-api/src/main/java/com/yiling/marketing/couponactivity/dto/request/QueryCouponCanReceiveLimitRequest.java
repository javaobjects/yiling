package com.yiling.marketing.couponactivity.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/4/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCouponCanReceiveLimitRequest extends BaseRequest {

    /**
     * 店铺企业ID
     */
    @NotNull
    private Long eid;

    /**
     * 商品ID
     */
    @NotNull
    private Long goodsId;

    /**
     * 平台类型：1-B2B；2-销售助手
     */
    @NotNull
    private Integer platformType;

    /**
     * 查询数量
     */
    @NotNull
    private Integer limit;

    /**
     * 企业类型
     */
    private Integer type;

    /**
     * 会员规格id
     */
    private Long memberId;

    /**
     * 推广方eid
     */
    private Long promotionEid;

    /**
     * 会员类型id集合
     */
    private List<Long> memberIds;

    /**
     * 推广人id
     */
    private List<Long> promotionEids;

    /**
     * 当前用户企业ID
     */
    private Long currentEid;

    /**
     * 店铺id集合
     */
    private List<Long> eids;
}
