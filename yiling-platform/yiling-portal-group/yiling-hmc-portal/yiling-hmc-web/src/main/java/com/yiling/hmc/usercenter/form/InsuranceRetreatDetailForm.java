package com.yiling.hmc.usercenter.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 退保查询 Form
 *
 * @author: fan.shen
 * @date: 2022/4/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsuranceRetreatDetailForm extends BaseForm {

    /**
     * 保单id
     */
    @ApiModelProperty("保单id")
    private Long id;


}
