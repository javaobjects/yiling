package com.yiling.f2b.admin.order.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/7/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryOrderReturnInfoForm extends QueryPageListForm {
    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id", required = true)
    private Long orderId;
}
