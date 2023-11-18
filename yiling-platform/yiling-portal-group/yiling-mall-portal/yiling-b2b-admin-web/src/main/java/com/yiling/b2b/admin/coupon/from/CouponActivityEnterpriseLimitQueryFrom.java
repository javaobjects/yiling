package com.yiling.b2b.admin.coupon.from;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/10/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class CouponActivityEnterpriseLimitQueryFrom extends QueryPageListForm {

    /**
     * id
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 优惠券活动ID
     */
    @NotNull
    private Long couponActivityId;

    /**
     * 企业id
     */
    @ApiModelProperty("企业id")
    private String eid;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String ename;

}
