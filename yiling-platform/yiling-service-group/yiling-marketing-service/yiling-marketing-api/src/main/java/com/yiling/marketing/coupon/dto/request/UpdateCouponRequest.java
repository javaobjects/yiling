package com.yiling.marketing.coupon.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateCouponRequest extends BaseRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 优惠券活动ID
     */
    private Long couponActivityId;

    /**
     * 优惠券活动名称
     */
    private String couponActivityName;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 获取方式（1-运营发放；2-自动发放；3-自主领取）
     */
    private Integer getType;

    /**
     * 自动发券/自主领券活动ID
     */
    private Long couponActivityAutoId;

    /**
     * 自动发券/自主领券活动名称
     */
    private String couponActivityAutoName;

    /**
     * 发放/领取人ID
     */
    private Long getUserId;

    /**
     * 发放/领取人人姓名
     */
    private String getUserName;

    /**
     * 发放/领取时间
     */
    private Date getTime;

    /**
     * 用券人ID
     */
    private Long userId;

    /**
     * 用券人姓名
     */
    private String userName;

    /**
     * 用券时间
     */
    private Date useTime;

    /**
     * 优惠券生效时间
     */
    private Date beginTime;

    /**
     * 优惠券失效时间
     */
    private Date endTime;

    /**
     * 使用状态：1-未使用；2-已使用
     */
    private Integer usedStatus;

    /**
     * 状态（1-正常；2-废弃）
     */
    private Integer status;

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