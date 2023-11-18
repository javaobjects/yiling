package com.yiling.b2b.admin.coupon.from;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
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
public class DeleteCouponActivityEnterpriseGiveRecordFrom extends BaseForm {

    /**
     * 优惠券活动ID
     */
    @ApiModelProperty("优惠券活动ID")
    @NotNull
    private Long couponActivityId;

    /**
     * ids
     */
    @ApiModelProperty("ids")
    @NotNull
    private List<Long> ids;
}
