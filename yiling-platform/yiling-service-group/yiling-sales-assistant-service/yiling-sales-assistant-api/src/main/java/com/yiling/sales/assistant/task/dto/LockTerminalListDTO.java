package com.yiling.sales.assistant.task.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2021/09/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class LockTerminalListDTO extends BaseDTO {

    private static final long serialVersionUID = -2493440078354288178L;

    /**
     * 终端主键
     */
    private Long terminalId;

    /**
     * 终端名称
     */
    private String terminalName;

    /**
     * 终端地址
     */
    private String terminalAddress;
}
