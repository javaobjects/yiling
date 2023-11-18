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
 * @date: 2021/11/4
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityAutoGiveFailPageVo extends BaseVO {

    /**
     * 自动发券活动ID
     */
    @ApiModelProperty("自动发券活动ID")
    private Long couponActivityAutoGiveId;

    /**
     * 自动发券活动名称
     */
    @ApiModelProperty("自动发券活动名称")
    private String couponActivityAutoGiveName;

    /**
     * 优惠券活动ID
     */
    @ApiModelProperty("优惠券活动ID")
    private Long couponActivityId;

    /**
     * 优惠券活动名称
     */
    @ApiModelProperty("优惠券活动名称")
    private String couponActivityName;

    /**
     * 获券企业id
     */
    @ApiModelProperty("获券企业id")
    private Long eid;

    /**
     * 获券企业名称
     */
    @ApiModelProperty("获券企业名称")
    private String ename;

    /**
     * 失败原因
     */
    @ApiModelProperty("失败原因")
    private String faileReason;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

}
