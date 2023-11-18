package com.yiling.sales.assistant.app.task.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author: ray
 * @date: 2021/9/18
 */
@Data
public class TaskTraceRegisterUserVO implements Serializable {
    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户手机号
     */
    private String mobile;

    /**
     * 注册时间
     */
    private Date registerTime;

}