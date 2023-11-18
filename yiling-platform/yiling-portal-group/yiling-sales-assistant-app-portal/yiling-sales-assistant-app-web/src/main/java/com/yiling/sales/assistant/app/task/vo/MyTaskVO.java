package com.yiling.sales.assistant.app.task.vo;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * app我的任务列表页实体
 * </p>
 *
 * @author gxl
 * @since 2021-09-30
 */
@Data
@ApiModel(value="任务列表", description="任务列表 ")
public class MyTaskVO {

    @ApiModelProperty(value = "用户承接的任务id[不等同于任务本身的id]")
    private Long id;

    @ApiModelProperty(value = "任务id")
    private Long taskId;

    @ApiModelProperty(value = "任务名称")
    private String taskName;
    @ApiModelProperty(value = "销售商品品种数")
    private Integer goodsCount;

    @ApiModelProperty(value = "收益")
    private String profit;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "任务类型：1-交易额 2-交易量 3-新用户推广 4-促销推广 5-会议推广 6-学术推广 7-新人推广 8 会员推广-购买 10-上传资料")
    private Integer finishType;
    
    @ApiModelProperty("任务状态：1-进行中 2-已结束 3-已结束（后台是停用）")
    private Integer taskStatus;

}
