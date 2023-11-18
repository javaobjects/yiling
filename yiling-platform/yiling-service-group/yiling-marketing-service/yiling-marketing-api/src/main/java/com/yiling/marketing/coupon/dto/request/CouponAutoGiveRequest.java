package com.yiling.marketing.coupon.dto.request;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponAutoGiveRequest extends BaseRequest {

    /**
     * 平台自动发券/自主领券活动ID
     */
    @NotNull
    private Long couponActivityAutoId;

    /**
     * 商家自主领券活动ID
     */
    private Long couponActivityBusinessAutoId;

    /**
     * 自动发券/自主领券活动名称
     */
    private String couponActivityAutoName;

    /**
     * 优惠券活动ID
     */
    @NotNull
    private Long couponActivityId;

    /**
     * 优惠券活动名称
     */
    private String couponActivityName;

    /**
     * 企业ID
     */
    @NotNull
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 获取方式（1-运营发放；2-自动发放；3-自主领取）
     */
    @NotNull
    private Integer getType;

    /**
     * 发放/领取人ID
     */
    @NotNull
    private Long getUserId;

    /**
     * 发放/领取人人姓名
     */
    private String getUserName;

    /**
     * 发放/领取时间
     */
    @NotNull
    private Date getTime;

    /**
     * 优惠券生效时间
     */
    @NotNull
    private Date beginTime;

    /**
     * 优惠券失效时间
     */
    @NotNull
    private Date endTime;

    /**
     * 创建人
     */
    @NotNull
    private Long createUser;

    /**
     * 创建时间
     */
    @NotNull
    private Date createTime;

    /**
     * 备注
     */
    private String remark;
}
