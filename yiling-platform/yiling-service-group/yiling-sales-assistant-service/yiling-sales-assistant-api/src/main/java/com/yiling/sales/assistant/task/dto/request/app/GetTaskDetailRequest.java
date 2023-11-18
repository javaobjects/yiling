package com.yiling.sales.assistant.task.dto.request.app;

import java.io.Serializable;

import lombok.Data;

/**
 * @author: ray
 * @date: 2021/9/28
 */
@Data
public class GetTaskDetailRequest implements Serializable {
    private static final long serialVersionUID = 9223016539860797659L;
    private Long taskId;

    private Long userId;
}