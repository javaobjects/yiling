package com.yiling.sales.assistant.task.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * <p>
 *  任务追踪-销售记录
 * </p>
 *
 * @author gxl
 * @since 2021-9-18
 */
@Data
public class TaskTraceOrderDTO implements Serializable {


    private static final long serialVersionUID = 695264686379531345L;
    private String orderNo;

    private String terminalName;

    private Date orderTime;


}
