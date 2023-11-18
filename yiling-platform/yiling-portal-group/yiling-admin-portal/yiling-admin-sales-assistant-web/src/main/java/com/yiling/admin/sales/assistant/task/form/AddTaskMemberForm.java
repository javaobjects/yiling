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
public class AddTaskMemberForm {
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