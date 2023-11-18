package com.yiling.admin.sales.assistant.task.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 任务列表页实体
 * </p>
 *
 * @author gxl
 * @since 2020-04-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="任务列表", description="任务列表 ")
public class TaskVO extends BaseVO {


    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "任务说明")
    private String taskDesc;

    @ApiModelProperty(value = "0单品任务 1多品销售")
    private Integer saleType;

    @ApiModelProperty(value = "多品销售种类数")
    private Integer categories;

    @ApiModelProperty(value = "企业名称")
    private String ename;

    @ApiModelProperty(value = "企业id,rk_enterprise主键")
    private Long eid;

    @ApiModelProperty(value = "销售区域")
    private String taskArea;

    @ApiModelProperty(value = "可参与任务人数")
    private Integer takeLimit;

    @ApiModelProperty(value = "每人可参与次数")
    private Integer timesLimit;


    @ApiModelProperty(value = "是否长期有效 0:否1：是")
    private Integer neverExpires;

    @ApiModelProperty(value = "开始时间 长期有效只有开始没有结束")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;



    @ApiModelProperty(value = "状态 0未开始1进行中2已结束3停用")
    private Integer taskStatus;

    @ApiModelProperty(value = "创建人")
    private String createdBy;



    @ApiModelProperty(value = "创建时间")
    private Date createdTime;

    @ApiModelProperty(value = "更新人")
    private String updatedBy;


    @ApiModelProperty(value = "更新时间")
    private Date updatedTime;

    @ApiModelProperty(value = "任务类型：1-交易类 2-推广类")
    private Integer category;

    @ApiModelProperty(value = "任务类型-子类型：1-交易额 2-交易量 3-新用户推广 4-促销推广 5-会议推广 6-学术推广 7-新人推广 8-会员推广-购买")
    private Integer finishType;
}
