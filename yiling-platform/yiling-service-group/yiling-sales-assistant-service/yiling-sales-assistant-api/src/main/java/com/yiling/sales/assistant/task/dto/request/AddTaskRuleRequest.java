package com.yiling.sales.assistant.task.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 创建任务规则
 * @author: ray
 * @date: 2021/9/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddTaskRuleRequest extends BaseRequest {

    private static final long serialVersionUID = 8116343126147147667L;

    private Integer ruleType;

    private String ruleKey;

    private String ruleValue;
}