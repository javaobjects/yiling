package com.yiling.workflow.workflow.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;

/**
 * 审批意见
 * @author: gxl
 * @date: 2023/3/17
 */
@Data
public class CommentDTO extends BaseDTO {
    private static final long serialVersionUID = -2489905907039668102L;
    private String fullMessage;
}