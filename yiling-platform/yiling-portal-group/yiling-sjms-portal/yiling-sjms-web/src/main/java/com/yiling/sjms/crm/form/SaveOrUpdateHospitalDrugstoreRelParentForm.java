package com.yiling.sjms.crm.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 父表单
 * @author: gxl
 * @date: 2023/2/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateHospitalDrugstoreRelParentForm extends BaseForm {

    /**
     * form表主键
     */
    @ApiModelProperty(value = "主表单id")
    private Long formId;

    @ApiModelProperty(value = "备注")
    @Length(max = 500)
    private String remark;

    @ApiModelProperty(value = "表单类型 13-院外药店关系变更", required = true)
    @NotNull
    private Integer formType;
}