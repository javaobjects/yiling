package com.yiling.sales.assistant.task.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2021/09/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TaskCountDTO extends BaseDTO {

    private static final long serialVersionUID = 4361330599414473640L;
    /**
     * 任务总数
     */
    private Long count;

    /**
     * 平台任务数
     */
    private Long platformCount;

    /**
     * 企业任务数
     */
    private Long enterpriseCount;
}
