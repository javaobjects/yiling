package com.yiling.marketing.couponactivity.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

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
public class QueryCouponActivityCanReceiveRequest extends BaseRequest {

    /**
     * 查询数量
     */
    @NotNull
    private Integer limit;

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
     * 店铺企业ID
     */
    private Long shopEid;

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
     * 1-全部用户；2-仅普通用户；3-全部会员用户；4-部分指定会员；5-部分用户;6-新客
     */
    private Integer conditionUserType;

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
     * 会员类型id-多会员以后变成了会员id集合
     */
    private List<Long> memberIds;

    /**
     * 推广人id
     */
    private Long promotionEid;

    /**
     * 多会员以后变成了推广人id集合
     */
    private List<Long> promotionEids;

    /**
     * 当前操作人ID
     */
     private Long userId;

    /**
     * 企业类型
     */
    private Integer etype;

    /**
     * ename
     */
    private String ename;

    /**
     * 当前用户是否为会员：0-否 1-是
     */
    private Integer currentMember;

    /**
     * 当前操作人姓名
     */
    private String userName;
}
