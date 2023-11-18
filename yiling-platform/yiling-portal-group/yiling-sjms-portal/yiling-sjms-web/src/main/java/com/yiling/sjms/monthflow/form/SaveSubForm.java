package com.yiling.sjms.monthflow.form;

import javax.validation.constraints.NotEmpty;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: gxl
 * @date: 2023/6/25
 */
@Data
public class SaveSubForm extends BaseForm {

    /**
     * 文件地址
     */
    @ApiModelProperty("osskey")
    @NotEmpty
    private String key;

    /**
     * 文件名称
     */
    @ApiModelProperty("文件名")
    @NotEmpty
    private String name;

}
