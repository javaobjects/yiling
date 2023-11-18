package com.yiling.sales.assistant.task.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *
 * 移除部门
 * @author: hongyang.zhang
 * @data: 2021/09/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteDeptRequest extends BaseRequest {

    private static final long serialVersionUID = 7782311420906477303L;

    /**
     * 任务与部门关系id
     */
    private Long taskDeptUserId;

}
