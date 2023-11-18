package com.yiling.sjms.flee.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2023/3/20 0020
 */
@Data
public class AppendixDetailForm extends BaseForm {

    @ApiModelProperty(value = "文件url")
    private String url;

    @ApiModelProperty(value = "文件url")
    private String key;

    @ApiModelProperty(value = "文件名称")
    private String name;
}
