package com.yiling.sjms.gb.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FileInfoForm extends BaseForm {

    /**
     * 文件名称
     */
    @ApiModelProperty(value = "文件名称")
    private String fileName;

    /**
     * 文件路径
     */
    @ApiModelProperty(value = "文件路径")
    private String fileUrl;

    /**
     * 文件路径
     */
    @ApiModelProperty(value = "文件MD5")
    private String fileMd5;
}
