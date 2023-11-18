package com.yiling.sales.assistant.task.dto.request;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 拉新人：无限制 任务进度计算请求
 * @author: ray
 * @date: 2021/9/26
 */
@Data
@Accessors(chain = true)
public class UpdateUserTaskNoLimitRequest implements Serializable {
    private static final long serialVersionUID = -8175908549733642644L;
    private Long userId;
    /**
     * 邀请人id 即队长id
     */
    private Long inviterUserId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 注册时间
     */
    private Date regTime;



    /**
     * 手机号
     */
    private String mobile;

    /**
     * sa_user_team 创建时间
     */
    private Date createTime;
}