package com.yiling.hmc.usercenter.form;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询订单详情 Form
 *
 * @author: fan.shen
 * @date: 2022/4/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryOrderDetailForm extends BaseForm {

    /**
     * 订单id
     */
    @ApiModelProperty("订单id")
    private Long id;

}
