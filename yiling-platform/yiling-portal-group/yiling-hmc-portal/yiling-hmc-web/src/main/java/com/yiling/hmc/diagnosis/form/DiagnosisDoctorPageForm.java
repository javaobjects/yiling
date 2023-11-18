package com.yiling.hmc.diagnosis.form;

import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 首页问诊医生 form
 *
 * @author: fan.shen
 * @date: 2024/5/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DiagnosisDoctorPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "搜索内容")
    @NotBlank(message = "搜索内容不能为空")
    private String content;

}