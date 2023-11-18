package com.yiling.admin.b2b.coupon.from;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class QueryCouponActivityAutoGivePageFrom extends QueryPageListForm {

    /**
     * 自动发放/领取活动id
     */
    @NotNull
    @ApiModelProperty("自动发放/领取活动id")
    private Long couponActivityAutoId;

}
