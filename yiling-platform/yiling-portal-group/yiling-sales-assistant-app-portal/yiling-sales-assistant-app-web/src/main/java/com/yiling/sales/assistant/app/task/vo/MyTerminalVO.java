package com.yiling.sales.assistant.app.task.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 当前用户已锁定终端
 * @author: ray
 * @data: 2021/09/15
 */
@Data
@Accessors(chain = true)
public class MyTerminalVO  {

    @ApiModelProperty(value = "终端名称")
    private String terminalName;

    @ApiModelProperty(value = "终端地址")
    private String terminalAddress;

    @ApiModelProperty(value = "联系人姓名")
    private String contactor;

    @ApiModelProperty(value = "联系人电话")
    private String contactorPhone;

}
