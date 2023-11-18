package com.yiling.admin.cms.content.form;

import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 搜索医生
 * @author: fan.shen
 * @date: 2022-10-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryDoctorPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "医生ID", example = "123")
    private Long doctorId;

    @ApiModelProperty(value = "医生名称", example = "张三")
    private String doctorName;

    @ApiModelProperty(value = "医生名称", example = "张三")
    private String hospitalName;
}