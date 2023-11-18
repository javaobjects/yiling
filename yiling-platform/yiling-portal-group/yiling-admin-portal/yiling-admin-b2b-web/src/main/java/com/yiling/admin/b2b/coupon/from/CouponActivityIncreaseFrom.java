package com.yiling.admin.b2b.coupon.from;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/10/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class CouponActivityIncreaseFrom extends BaseForm {


    /**
     * 主键ID
     */
    @NotNull
    private Long id;

    /**
     * 数量
     */
    @NotNull
    private Integer quantity;

    /**
     * 备注
     */
    private String remark;

}
