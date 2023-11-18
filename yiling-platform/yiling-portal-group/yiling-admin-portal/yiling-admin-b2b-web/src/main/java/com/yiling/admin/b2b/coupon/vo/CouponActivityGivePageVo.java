package com.yiling.admin.b2b.coupon.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/3
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityGivePageVo extends BaseVO {

    /**
     * 优惠券活动id
     */
    @ApiModelProperty(value = "优惠券活动id")
    private Long couponActivityId;

    /**
     * 优惠规则
     */
    @ApiModelProperty(value = "优惠规则")
    private String couponRules;

    /**
     * 获券企业id
     */
    @ApiModelProperty(value = "获券企业id")
    private Long eid;

    /**
     * 获券企业名称
     */
    @ApiModelProperty(value = "获券企业名称")
    private String ename;

    /**
     * 发放方式（1-运营发放；2-自动发放；3-自主领取）
     */
    @ApiModelProperty(value = "发放方式（1-运营发放；2-自动发放；3-自主领取 4-满赠活动赠送）")
    private Integer getType;

    /**
     * 使用状态（1-未使用；2-已使用）
     */
    @ApiModelProperty(value = "使用状态（1-未使用；2-已使用）")
    private Integer usedStatus;

    /**
     * 是否作废（1-正常；2-废弃）
     */
    @ApiModelProperty(value = "是否作废（1-正常；2-废弃）")
    private Integer status;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间（有效期信息）")
    private Date beginTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间（有效期信息）")
    private Date endTime;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期")
    private String effectiveTime;

    /**
     * 发放/领取时间
     */
    @ApiModelProperty(value = "发放/领取时间")
    private Date getTime;

    /**
     * 发放人id
     */
    @ApiModelProperty(value = "发放/领取人id")
    private Long getUserId;

    /**
     * 发放人姓名
     */
    @ApiModelProperty(value = "发放/领取人姓名")
    private String getUserName;

}
