package com.yiling.sales.assistant.task.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * <p>
 * 任务规则 -参与条件
 * </p>
 *
 * @author gxl
 * @since 2020-04-21
 */
@Data
public class TaskRuleDTO implements Serializable {
    private static final long serialVersionUID = -5477402392170736196L;
    private Long id;

    private String ruleKey;

    private String ruleValue;



}
