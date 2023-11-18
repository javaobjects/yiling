package com.yiling.sales.assistant.app.task.vo;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * app任务列表页实体
 * </p>
 *
 * @author gxl
 * @since 2020-04-23
 */
@Data
@ApiModel(value="任务列表", description="任务列表 ")
public class TaskVO{


    private Long id;

    @ApiModelProperty(value = "任务名称")
    private String taskName;


    @ApiModelProperty(value = "销售商品品种数")
    private Integer goodsCount;



    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "收益")
    private String profit;




    @ApiModelProperty(value = "是否全国范围销售 0-否 1-是")
    private Integer fullCover;

    @ApiModelProperty(value = "是否已承接")
    private Boolean taked;

    @ApiModelProperty(value = "任务类型：1-交易额 2-交易量 3-新用户推广 4-促销推广 5-会议推广 6-学术推广 7-新人推广 8 会员推广-购买 10-上传资料")
    private Integer finishType;

}
