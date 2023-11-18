package com.yiling.admin.hmc.insurance.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询保单详情 Form
 *
 * @author: fan.shen
 * @date: 2022/4/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryInsuranceRecordDetailForm extends BaseForm {

    /**
     * 保单id
     */
    @ApiModelProperty("保单id")
    private Long id;

}
