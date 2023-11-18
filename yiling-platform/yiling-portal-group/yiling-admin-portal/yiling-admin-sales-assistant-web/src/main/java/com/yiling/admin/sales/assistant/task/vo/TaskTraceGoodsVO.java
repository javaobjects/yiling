package com.yiling.admin.sales.assistant.task.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 任务追踪-商品明细
 * </p>
 *
 * @author gxl
 * @since 2021-9-18
 */
@Data
public class TaskTraceGoodsVO implements Serializable {


    private static final long serialVersionUID = 2622743496844217697L;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "值类型 0 交易额  1交易量")
    private Integer valueType;

    @ApiModelProperty(value = "完成值")
    private String finishValue;

    @ApiModelProperty(value = "目标值")
    private String goalValue;

    @ApiModelProperty(value = "百分比")
    private String percent;


}
