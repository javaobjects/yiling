package com.yiling.sales.assistant.task.dto.request.app;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:gxl
 * @description:我的任务详情查询参数
 */
@Data
@Accessors(chain = true)
public class QueryMyTaskDetailRequest implements Serializable {


    private static final long serialVersionUID = -72225603568467625L;
    private Long userId;

    private Long userTaskId;

    private Long eid;
}
