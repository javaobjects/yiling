package com.yiling.sales.assistant.task.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: ray
 * @date: 2021/12/17
 */
@Data
@Accessors(chain = true)
public class UpdateTaskMemberRequest extends BaseRequest {
    private static final long serialVersionUID = -6742266405875327142L;
    private Long memberId;

    private Long memberStageId;

    /**
     * 海报url
     */
    private String playbill;

    private Long id;
    
}