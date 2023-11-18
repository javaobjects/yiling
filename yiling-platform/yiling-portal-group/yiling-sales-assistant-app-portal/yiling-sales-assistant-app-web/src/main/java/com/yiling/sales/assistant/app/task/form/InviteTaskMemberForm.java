package com.yiling.sales.assistant.app.task.form;

import com.yiling.framework.common.base.form.BaseForm;

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
public class InviteTaskMemberForm extends BaseForm {

  private Long userTaskId;

  private Long taskId;
}