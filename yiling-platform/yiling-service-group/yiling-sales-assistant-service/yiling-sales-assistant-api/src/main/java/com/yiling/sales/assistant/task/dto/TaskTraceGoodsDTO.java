package com.yiling.sales.assistant.task.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 任务追踪-商品明细
 * </p>
 *
 * @author gxl
 * @since 2021-9-18
 */
@Data
@Accessors(chain = true)
public class TaskTraceGoodsDTO  extends BaseDTO {

    private String goodsName;

    private Integer valueType;

    private String finishValue;

    private String goalValue;

    private String percent;

}
