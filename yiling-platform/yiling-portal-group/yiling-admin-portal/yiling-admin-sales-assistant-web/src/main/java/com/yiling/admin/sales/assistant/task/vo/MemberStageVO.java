package com.yiling.admin.sales.assistant.task.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author: ray
 * @date: 2021/12/17
 */
@Data
@ApiModel
public class MemberStageVO {
    private Long memberStageId;

    private BigDecimal price;

    private Integer validTime;
}