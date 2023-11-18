package com.yiling.sales.assistant.task.dto;

import java.io.Serializable;

import com.yiling.user.system.enums.UserTypeEnum;

import lombok.Data;

/**
 * @author: gxl
 * @date: 2022/4/27
 */
@Data
public class UserTypeDTO implements Serializable {
    private UserTypeEnum userTypeEnum;

    /**
     * 承接任务时所属企业
     */
    private Long currentEid;

    private Long userId;
}