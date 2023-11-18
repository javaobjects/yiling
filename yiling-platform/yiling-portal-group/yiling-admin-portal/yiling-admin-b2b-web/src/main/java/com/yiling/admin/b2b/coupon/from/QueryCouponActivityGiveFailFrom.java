package com.yiling.admin.b2b.coupon.from;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

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
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class QueryCouponActivityGiveFailFrom extends QueryPageListForm {

    /**
     * 自动发券活动ID
     */
    @ApiModelProperty("自动发券活动ID")
    @NotNull
    private Long autoGiveId;

    /**
     * 自动发券活动名称
     */
    @ApiModelProperty("自动发券活动名称")
    private String autoGiveName;

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
     * 创建时间-开始时间
     */
    @ApiModelProperty("创建时间-开始时间")
    private Date beginCreateTime;

    /**
     * 创建时间-结束时间
     */
    @ApiModelProperty("创建时间-结束时间")
    private Date endCreateTime;

}
