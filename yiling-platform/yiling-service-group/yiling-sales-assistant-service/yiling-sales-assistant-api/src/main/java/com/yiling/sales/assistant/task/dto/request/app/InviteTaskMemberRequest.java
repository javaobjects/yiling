package com.yiling.sales.assistant.task.dto.request.app;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: ray
 * @date: 2021/12/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InviteTaskMemberRequest extends BaseRequest {

  private Long userTaskId;

  private Long taskId;

  private Long eid;
}