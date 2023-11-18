package com.yiling.sales.assistant.app.task.vo;

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
public class TaskMemberPromotiondVO  {
    private Long promotionActivityId;

    private Long goodsGiftId;

    private String name;

    private Date beginTime;

    private Date endTime;

    private BigDecimal promotionAmount;

    private String giftName;
}