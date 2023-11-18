package com.yiling.sjms.flee.vo;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.sjms.form.vo.FormVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2023/3/11 0011
 */
@Data
public class AppendixDetailVO extends BaseVO {

    @ApiModelProperty(value = "文件url")
    private String url;

    @ApiModelProperty(value = "文件url")
    private String key;

    @ApiModelProperty(value = "文件名称")
    private String name;
}
