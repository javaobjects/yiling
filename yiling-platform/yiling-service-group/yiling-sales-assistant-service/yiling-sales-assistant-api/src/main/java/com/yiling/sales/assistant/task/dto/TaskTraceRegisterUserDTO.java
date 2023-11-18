package com.yiling.sales.assistant.task.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author: ray
 * @date: 2021/9/18
 */
@Data
public class TaskTraceRegisterUserDTO implements Serializable {
    private static final long serialVersionUID = -6048791914627281338L;
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


    private Long userId;

}