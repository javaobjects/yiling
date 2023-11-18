package com.yiling.sales.assistant.task.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 任务追踪-拉户类
 * @author: ray
 * @date: 2021/9/18
 */
@Data
public class TaskTraceTerminalDTO implements Serializable {
    private static final long serialVersionUID = -3097734113055229563L;

    /**
     * 终端名称
     */
    private String terminalName;

    /**
     * 终端联系人
     */
    private String contactor;

    /**
     * 终端联系人手机号
     */
    private String contactorMobile;
}