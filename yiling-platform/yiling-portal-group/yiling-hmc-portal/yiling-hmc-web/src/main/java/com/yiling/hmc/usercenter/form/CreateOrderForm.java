package com.yiling.hmc.usercenter.form;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 创建订单 Form
 *
 * @author: fan.shen
 * @date: 2022/4/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateOrderForm extends BaseForm {

    /**
     * 参保记录id
     */
    @ApiModelProperty("参保记录id")
    private Long insuranceRecordId;

}
