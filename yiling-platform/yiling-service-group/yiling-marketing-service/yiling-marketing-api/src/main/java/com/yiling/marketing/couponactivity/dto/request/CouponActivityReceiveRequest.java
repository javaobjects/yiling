package com.yiling.marketing.couponactivity.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityReceiveRequest extends BaseRequest {

    /**
     * 优惠券活动ID
     */
    @NotNull
    private Long couponActivityId;

    /**
     * 当前操作人ID
     */
    @NotNull
    private Long userId;

    /**
     * 当前操作人姓名
     */
    private String userName;

    /**
     * 当前企业ID
     */
    @NotNull
    private Long eid;

    /**
     * 当前企业名称
     */
    private String ename;

    /**
     * 企业类型
     */
    @NotNull
    private Integer etype;

    /**
     * 当前用户是否为会员：0-否 1-是
     */
    @NotNull
    private Integer currentMember;

    /**
     * 平台类型：1-B2B；2-销售助手
     */
    @NotNull
    private Integer platformType;

}
