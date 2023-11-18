package com.yiling.sales.assistant.app.task.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author:gxl
 * @description:已选终端
 * @date: Created in 9:47 2020/4/27
 * @modified By:
 */

@Data
public class SelectedTerminalVO {

    @ApiModelProperty(value = "终端名字")
    private String terminalName;
    @ApiModelProperty(value = "终端地址")
    private String terminalAddress;

    @ApiModelProperty(value = "终端id")
    private Long taskTerminalId;
}
