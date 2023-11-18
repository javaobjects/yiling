package com.yiling.workflow.workflow.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/12/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ProcessInstanceDTO extends BaseDTO {


    private static final long serialVersionUID = -7614091392752086678L;

    /**
     * 发起人
     */
    private String startUserId;
    /**
     * 发起时间
     */
    private Date startTime;

    private String processDefinitionName;

    private String processInstanceId;
}