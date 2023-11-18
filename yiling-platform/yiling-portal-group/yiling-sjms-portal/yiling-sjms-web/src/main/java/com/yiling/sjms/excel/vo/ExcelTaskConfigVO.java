package com.yiling.sjms.excel.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-11-17
 */
@Data
public class ExcelTaskConfigVO {

    @ApiModelProperty("业务编码")
    private String excelCode;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "描述")
    private List<String> descriptionList;

    @ApiModelProperty(value = "限制文件类型")
    private String limitType;

    @ApiModelProperty(value = "限制文件大小(KB)")
    private Integer limitSize;

    @ApiModelProperty(value = "模板地址")
    private String templateUrl;

}
