package com.yiling.sales.assistant.task.dto.app;

import java.io.Serializable;

import lombok.Data;

/**
 * @author: gxl
 * @date: 2022/3/3
 */
@Data
public class MyTaskProgressStepDTO implements Serializable {
    private static final long serialVersionUID = -1597387203286605467L;
    private String  percent;

    private Boolean isFinished;
}