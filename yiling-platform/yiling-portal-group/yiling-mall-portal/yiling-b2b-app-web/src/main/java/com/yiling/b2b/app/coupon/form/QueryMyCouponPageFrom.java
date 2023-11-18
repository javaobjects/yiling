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
public class QueryMyCouponPageFrom extends QueryPageListForm {

    /**
     * 状态类型：1-未使用；2-已使用；3-已过期
     */
    @NotNull
    @ApiModelProperty(required = true, value = "状态：1-未使用；2-已使用；3-已过期")
    private Integer usedStatusType;

}
