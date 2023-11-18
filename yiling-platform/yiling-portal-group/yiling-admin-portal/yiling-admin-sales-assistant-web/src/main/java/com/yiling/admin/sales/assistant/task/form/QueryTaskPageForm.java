package com.yiling.admin.sales.assistant.task.form;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: ray
 * @date: 2021/9/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="任务列表参数", description="任务列表参数 ")
public class QueryTaskPageForm extends QueryPageListForm {
    @NotNull
    @ApiModelProperty(value = "任务主体 0:平台任务1：企业任务")
    private Integer taskType;

    @ApiModelProperty(value = "任务名称")
    private String taskName;


    @ApiModelProperty(value = "省份")
    private String provinceCode;

    @ApiModelProperty(value = "市")
    private String cityCode;

    @ApiModelProperty(value = "任务类型：1-交易额 2-交易量 3-新用户推广 4-促销推广 5-会议推广 6-学术推广 7-新人推广 8-会员推广-购买")
    private Integer finishType;

/*    @ApiModelProperty(value = "区")
    private String regionCode;*/




    @ApiModelProperty(value = "任务开始时间-起 ")
    private Date startTime;

    @ApiModelProperty(value = "任务开始时间-止")
    private Date endTime;


    @ApiModelProperty(value = "任务结束时间-起 ")
    private Date starteTime;

    @ApiModelProperty(value = "任务结束时间-止")
    private Date endeTime;

    @ApiModelProperty(value = "创建时间开始")
    private Date startcTime;

    @ApiModelProperty(value = "创建时间结束")
    private Date endcTime;

    @ApiModelProperty(value = "状态 0未开始1进行中2已结束3停用")
    private Integer taskStatus;

    @ApiModelProperty(value = "创建人")
    private String createdBy;
}