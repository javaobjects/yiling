package com.yiling.sales.assistant.app.task.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 我的任务-进度
 * @author ray
 */
@Data
public class MyTaskProgressVO {


    @ApiModelProperty(value = "已完成值")
    private String finishValue;

    @ApiModelProperty(value = "完成任务商品数量")
    private Integer finishGoods;

    @ApiModelProperty(value = "任务包涵商品总数")
    private Integer taskGoodsTotal;

    @ApiModelProperty(value = "目标值（金额转成分存储）")
    private String goal;

    @ApiModelProperty(value = "百分比")
    private String percent;

    @ApiModelProperty(value = "是否每个品种都达到目标值（是的话可展开详情）")
    private Boolean eachCompute;

    @ApiModelProperty(value = "阶梯任务目标值各占的百分比")
    private List<MyTaskProgressStepVO> percentList;

    @ApiModelProperty(value = "未完成值")
    private String unfinish;


}
