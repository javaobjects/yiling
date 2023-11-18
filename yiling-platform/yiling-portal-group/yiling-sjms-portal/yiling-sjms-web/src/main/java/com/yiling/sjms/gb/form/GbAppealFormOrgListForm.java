package com.yiling.sjms.gb.form;

import javax.validation.constraints.NotBlank;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/5/18
 */
@Data
public class GbAppealFormOrgListForm extends BaseForm {

    /**
     * 部门名称
     */
    @NotBlank
    @ApiModelProperty(value = "部门名称")
    private String orgName;

}
