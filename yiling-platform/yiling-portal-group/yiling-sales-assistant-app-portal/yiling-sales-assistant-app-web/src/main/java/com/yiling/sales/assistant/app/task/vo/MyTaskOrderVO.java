package com.yiling.sales.assistant.app.task.vo;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author gaoxinlei
 */
@Data
public class MyTaskOrderVO  {


    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "终端名称")
    private String terminalName;

    @ApiModelProperty(value = "时间")
    private Date orderTime;


}
