package com.yiling.admin.b2b.coupon.from;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class QueryMemberCouponActivityFrom extends QueryPageListForm {

    /**
     * 会员名称
     */
    @ApiModelProperty("会员名称")
    private String name;

    /**
     * 会员名称
     */
    @ApiModelProperty("优惠券id")
    private Long id;
}
