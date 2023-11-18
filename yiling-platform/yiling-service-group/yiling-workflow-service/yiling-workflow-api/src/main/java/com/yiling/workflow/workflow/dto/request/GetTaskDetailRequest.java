package com.yiling.workflow.workflow.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 流程节点任务详情
 * @author: gxl
 * @date: 2023/2/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GetTaskDetailRequest extends BaseRequest {

    private static final long serialVersionUID = -974177429499348048L;

    private String taskId;

    /**
     * 用户工号
     */
    private String currentUserCode;

}