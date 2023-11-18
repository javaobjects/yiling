package com.yiling.sales.assistant.task.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @author: gxl
 * @date: 2023/1/9
 */
@Data
public class TaskDeptDTO implements Serializable {

    private static final long serialVersionUID = -9158244934231355438L;

    private Long id;

    private List<Long> children;
}