package com.yiling.workflow.workflow.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * @author: gxl
 * @date: 2022/11/30
 */
@Data
public class WorkFlowNodeDTO implements Serializable {

    private static final long serialVersionUID = -8499980930807254087L;
    private String nodeId;
    private String nodeName;


}