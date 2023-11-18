package com.yiling.admin.sales.assistant.task.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: ray
 * @date: 2021/12/20
 */
@Data
@Accessors(chain = true)
public class AddTaskMemberPromotionForm {
    @ApiModelProperty(value = "活动id")
    private Long promotionActivityId;

    @ApiModelProperty(value = "赠品信息表id")
    private Long goodsGiftId;
}