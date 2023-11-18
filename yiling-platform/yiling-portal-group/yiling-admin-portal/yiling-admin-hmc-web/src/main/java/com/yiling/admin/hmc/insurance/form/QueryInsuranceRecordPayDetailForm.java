package com.yiling.admin.hmc.insurance.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询保单支付详情 Form
 *
 * @author: fan.shen
 * @date: 2022/4/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryInsuranceRecordPayDetailForm extends BaseForm {

    /**
     * 保单id
     */
    @ApiModelProperty("支付单id")
    private Long recordPayId;

}
