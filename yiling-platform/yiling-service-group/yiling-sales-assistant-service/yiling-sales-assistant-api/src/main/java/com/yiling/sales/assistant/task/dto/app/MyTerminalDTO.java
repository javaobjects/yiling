package com.yiling.sales.assistant.task.dto.app;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 当前用户已锁定终端
 * @author: ray
 * @data: 2021/10/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class MyTerminalDTO extends BaseDTO {

    private static final long serialVersionUID = -5811441058254336840L;
    private String terminalName;

    private String terminalAddress;

    private String contactor;

    private String contactorPhone;

}
