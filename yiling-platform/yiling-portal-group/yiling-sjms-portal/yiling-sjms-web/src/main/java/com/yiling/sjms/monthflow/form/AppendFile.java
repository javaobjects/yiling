package com.yiling.sjms.monthflow.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: gxl
 * @date: 2023/6/29
 */
@Data
public class AppendFile {
    @ApiModelProperty(value = "文件url")
    private String key;

    @ApiModelProperty(value = "文件名称")
    private String name;
}