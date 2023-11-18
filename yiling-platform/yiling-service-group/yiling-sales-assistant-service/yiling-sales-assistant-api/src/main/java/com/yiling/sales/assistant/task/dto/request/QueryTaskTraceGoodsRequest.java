package com.yiling.sales.assistant.task.dto.request;

import java.io.Serializable;

import lombok.Data;

/**
 * 任务追踪-任务品完成进度查询参数
 * @author gaoxinlei
 */
@Data
public class QueryTaskTraceGoodsRequest implements Serializable {


    private static final long serialVersionUID = -1369108555166784729L;
    private Long userTaskId;
}
