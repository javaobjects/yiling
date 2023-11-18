package com.yiling.marketing.couponactivity.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityGiveDTO extends BaseDTO {

    /**
     * 优惠券活动id
     */
    private Long couponActivityId;

    /**
     * 优惠规则
     */
    private String couponRules;

    /**
     * 获券企业id
     */
    private Long eid;

    /**
     * 获券企业名称
     */
    private String ename;

    /**
     * 发放方式（1-运营发放；2-自动发放；3-自主领取）
     */
    private Integer getType;


    /**
     * 发放方式（1-运营发放；2-自动发放；3-自主领取）
     */
    private String getTypeStr;

    /**
     * 使用状态
     */
    private Integer usedStatus;

    /**
     * 使用状态
     */
    private String usedStatusStr;

    /**
     * 是否作废（1-正常；2-废弃）
     */
    private Integer status;

    /**
     * 是否作废（1-正常；2-废弃）
     */
    private String statusStr;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 有效期
     */
    private String effectiveTime;

    /**
     * 发放时间
     */
    private Date getTime;

    /**
     * 发放人id
     */
    private Long getUserId;

    /**
     * 发放人姓名
     */
    private String getUserName;
}
