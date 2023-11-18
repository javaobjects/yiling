package com.yiling.sales.assistant.task.dto.request;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: ray
 * @date: 2021/12/20
 */
@Data
@Accessors(chain = true)
public class AddTaskMemberPromotionRequest implements Serializable {
    private static final long serialVersionUID = 8478107025434664370L;
    private Long promotionActivityId;

    private Long goodsGiftId;
}