package com.yiling.sales.assistant.task.dto.request.app;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询我的任务列表入参
 * @author: ray
 * @date: 2021/9/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMyTaskRequest extends QueryPageListRequest {
    @NotNull
    private Integer taskType;
}