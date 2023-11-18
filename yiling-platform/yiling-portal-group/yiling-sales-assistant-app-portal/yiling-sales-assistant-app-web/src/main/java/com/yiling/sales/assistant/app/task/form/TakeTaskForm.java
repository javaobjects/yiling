package com.yiling.sales.assistant.app.task.form;


import com.yiling.framework.common.base.form.BaseForm;

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
public class TakeTaskForm extends BaseForm {


    private Long taskId;

    /*private List<SelectedTerminalForm> selectedTerminalList;*/
}
