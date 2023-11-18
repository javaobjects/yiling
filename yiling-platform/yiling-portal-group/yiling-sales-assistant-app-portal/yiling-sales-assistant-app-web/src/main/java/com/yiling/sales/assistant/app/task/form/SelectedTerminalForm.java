package com.yiling.sales.assistant.app.task.form;

import lombok.Data;

/**
 * 承接任务-所选终端
 * @author ray
 */
@Data
public class SelectedTerminalForm {


    private String terminalName;

    private Long terminalId;

    private String terminalAddress;
}
