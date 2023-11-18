package com.yiling.sales.assistant.task.dto.request;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: ray
 * @date: 2021/9/14
 */
@Data
@Accessors(chain = true)
public class UpdateTaskRuleRequest implements Serializable {
    private static final long serialVersionUID = -6475188361523281701L;

    private String ruleValue;

    private Long taskRuleId;
}