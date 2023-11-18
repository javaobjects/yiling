package com.yiling.sales.assistant.task.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: ray
 * @date: 2021/12/20
 */
@Data
@Accessors(chain = true)
public class TaskMemberPromotiondDTO implements Serializable {
    private static final long serialVersionUID = -4029903771135186319L;
    private Long promotionActivityId;

    private Long goodsGiftId;

    private String name;

    private Date beginTime;

    private Date endTime;

    private BigDecimal promotionAmount;

    private String giftName;
}