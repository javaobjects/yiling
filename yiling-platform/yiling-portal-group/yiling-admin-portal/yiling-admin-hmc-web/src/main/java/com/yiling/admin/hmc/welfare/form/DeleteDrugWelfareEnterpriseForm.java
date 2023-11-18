package com.yiling.admin.hmc.welfare.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/09/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteDrugWelfareEnterpriseForm extends BaseForm {

    @ApiModelProperty("福利计划与商家关系id")
    private Long id;
}
