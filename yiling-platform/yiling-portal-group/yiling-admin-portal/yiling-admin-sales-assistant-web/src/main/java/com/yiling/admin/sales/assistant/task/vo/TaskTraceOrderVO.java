package com.yiling.admin.sales.assistant.task.vo;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *  任务追踪-销售记录
 * </p>
 *
 * @author gxl
 * @since 2021-9-18
 */
@Data
public class TaskTraceOrderVO  {

    @ApiModelProperty(value = "订单编号")
    private String orderNo;
    @ApiModelProperty(value = "终端名称")
    private String terminalName;
    @ApiModelProperty(value = "下单时间")
    private Date orderTime;


}
