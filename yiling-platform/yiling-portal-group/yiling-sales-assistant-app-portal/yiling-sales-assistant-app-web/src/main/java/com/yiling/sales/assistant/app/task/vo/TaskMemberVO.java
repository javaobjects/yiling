package com.yiling.sales.assistant.app.task.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 任务选择的会员和条件
 * @author: ray
 * @date: 2021/12/23
 */
@Data
public class TaskMemberVO extends BaseVO {

    private Long memberId;
    /**
     * 会员名称
     */
    @ApiModelProperty("会员名称")
    private String name;

    /**
     * 价格
     */
    @ApiModelProperty("价格")
    private BigDecimal price;

    /**
     * 有效时长
     */
    @ApiModelProperty("有效时长")
    private Integer validTime;

    private Long memberStageId;
    @ApiModelProperty("海报")
    private String playbill;

}