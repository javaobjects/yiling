package com.yiling.admin.sales.assistant.task.vo;

import lombok.Data;

/**
 * 任务追踪-拉户类
 * @author: ray
 * @date: 2021/9/18
 */
@Data
public class TaskTraceTerminalVO {

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