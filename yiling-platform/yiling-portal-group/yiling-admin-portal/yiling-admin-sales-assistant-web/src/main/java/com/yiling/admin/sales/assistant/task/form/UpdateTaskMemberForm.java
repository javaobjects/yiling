package com.yiling.admin.sales.assistant.task.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: ray
 * @date: 2021/12/17
 */
@Data
@Accessors(chain = true)
public class UpdateTaskMemberForm {
    @ApiModelProperty(value = "任务关联会员id,非任务id")
    private Long id;

    @ApiModelProperty(value = "会员id")
    private Long memberId;

    @ApiModelProperty(value = "购买门槛id")
    private Long memberStageId;

    /**
     * 海报url
     */
    @ApiModelProperty(value = "海报url")
    private String playbill;
    
}