package com.yiling.b2b.app.coupon.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCouponGoodsSearchFrom extends QueryPageListForm {

    /**
     * 优惠券Id
     */
    @NotNull
    @ApiModelProperty(value = "优惠券Id")
    private Long couponId;

    /**
     * 搜索词
     */
    @ApiModelProperty(value = "搜索词")
    private String key;

}
