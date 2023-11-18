package com.yiling.sales.assistant.task.dto.request.app;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.user.system.enums.UserTypeEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * app任务列表查询参数实体
 * </p>
 *
 * @author gxl
 * @since 2020-04-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryTaskPageRequest extends QueryPageListRequest {


    private Integer taskType;

    private Long userId;

    private Long eid;

    /**
     * 用户类型
     */
    private UserTypeEnum userType;
}
