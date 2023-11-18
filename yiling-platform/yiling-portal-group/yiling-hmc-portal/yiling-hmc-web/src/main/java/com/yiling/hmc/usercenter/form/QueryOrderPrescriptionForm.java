package com.yiling.hmc.usercenter.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询订单处方详情 Form
 *
 * @author: fan.shen
 * @date: 2022/4/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryOrderPrescriptionForm extends BaseForm {

    /**
     * 订单id
     */
    @ApiModelProperty("订单id")
    private Long id;

}
