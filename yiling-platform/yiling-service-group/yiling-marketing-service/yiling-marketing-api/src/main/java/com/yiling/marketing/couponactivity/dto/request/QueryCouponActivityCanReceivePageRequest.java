package com.yiling.marketing.couponactivity.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCouponActivityCanReceivePageRequest extends QueryPageListRequest {

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 当前用户企业ID
     */
    @NotNull
    private Long currentEid;

    /**
     * 店铺企业ID
     */
    @NotNull
    private Long eid;

    /**
     * 当前用户id
     */
    @NotNull
    private Long userId;

    /**
     * 业务类型：1-商品；2-店铺
     */
    private Integer businessType;

    /**
     * 平台类型：1-B2B；2-销售助手
     */
    @NotNull
    private Integer platformType;

    /**
     * 类型：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
     */
    private Integer type;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 会员类型id
     */
    private Long memberId;

    /**
     * 会员类型id集合
     */
    private List<Long> memberIds;

    /**
     * 推广人id
     */
    private Long promotionEid;

    /**
     * 推广人id
     */
    private List<Long> promotionEids;
}
