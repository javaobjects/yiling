package com.yiling.sales.assistant.task.dto.request.app;


import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.user.system.enums.UserTypeEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * @author:gxl
 * @description: 承接任务入参
 * @date: Created in 12:55 2020/4/26
 * @modified By:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TakeTaskRequest extends BaseRequest {

    private static final long serialVersionUID = -4234840603177135624L;
    private Long userId;

    private Long taskId;

    private List<SelectedTerminalRequest> selectedTerminalList;

    /**
     * 用户类型
     */
    private UserTypeEnum userType;

    private Long currentEid;
}
