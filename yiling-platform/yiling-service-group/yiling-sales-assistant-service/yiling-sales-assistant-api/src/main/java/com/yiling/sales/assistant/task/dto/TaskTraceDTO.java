package com.yiling.sales.assistant.task.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 任务追踪返回实体
 * @author: ray
 * @date: 2021/9/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TaskTraceDTO extends BaseDTO {
    private static final long serialVersionUID = 8384313237595536908L;
    private Integer takeCount;

    private Integer finishCount;

    private Integer userCount;

    private Integer enterpriseCount;

    private Integer memberBuyCount;

}