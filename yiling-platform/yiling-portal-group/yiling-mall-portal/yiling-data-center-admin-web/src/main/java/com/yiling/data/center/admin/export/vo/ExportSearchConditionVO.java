package com.yiling.data.center.admin.export.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/6/8
 */
@Data
public class ExportSearchConditionVO {

    @ApiModelProperty(value = "字段的英文名称")
    private String name;

    @ApiModelProperty(value = "字段的中文名")
    private String desc;

    @ApiModelProperty(value = "字段的值")
    private String value;

    @ApiModelProperty(value = "字段的值中文描述。比如：0否1是")
    private String valueDescription;

    @ApiModelProperty(value = "是否显示。隐藏字段传0，显示字段传1")
    private Integer visibility;
}
