package com.yiling.admin.sales.assistant.task.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2021/09/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class LockTerminalListVO extends BaseVO {

    @ApiModelProperty(value = "终端名称")
    private String terminalName;

    @ApiModelProperty(value = "终端地址")
    private String terminalAddress;

    @ApiModelProperty(value = "联系人姓名")
    private String contactor;

    @ApiModelProperty(value = "联系人电话")
    private String contactorPhone;

}
