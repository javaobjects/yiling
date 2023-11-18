package com.yiling.sales.assistant.task.dto.request.app;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 承接任务-所选终端
 * @author ray
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SelectedTerminalRequest extends BaseRequest {


    private static final long serialVersionUID = 7432277118697742278L;
    private String terminalName;

    private Long terminalId;

    private String terminalAddress;
}
