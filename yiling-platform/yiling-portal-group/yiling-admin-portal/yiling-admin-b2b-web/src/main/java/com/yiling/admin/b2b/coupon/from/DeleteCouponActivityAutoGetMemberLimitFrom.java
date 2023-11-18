package com.yiling.admin.b2b.coupon.from;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteCouponActivityAutoGetMemberLimitFrom extends BaseForm {

    /**
     * 自动发券活动ID
     */
    @ApiModelProperty("自动发券活动ID")
    @NotNull
    private Long couponActivityAutoGetId;

    /**
     * ids
     */
    @ApiModelProperty("ids")
    @NotNull
    private List<Long> ids;
}
