package com.yiling.sales.assistant.task.dto.request.app;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 编辑任务反显
 * @author: ray
 * @date: 2021/10/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryTaskDistributorPageRequest extends QueryPageListRequest {


    private static final long serialVersionUID = 8530176311715319855L;
    private Long taskId;

    private String name;
}