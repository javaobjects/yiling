package com.yiling.sales.assistant.task.dto.app;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 承接任务-所选终端
 * @author ray
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SelectedTerminalDTO extends BaseDTO {


    private String terminalName;

    private Long terminalId;

    private String terminalAddress;
}
