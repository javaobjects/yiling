package com.yiling.sales.assistant.task.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 任务追踪查询参数
 * @author: ray
 * @date: 2021/9/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryTaskTraceTaskUserRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -4727629618388642679L;

    private Long taskId;

    private String name;

    private String mobile;

    private Date startTime;

    private Date endTime;
}