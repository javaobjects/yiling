package com.yiling.sales.assistant.task.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 任务区域
 * @author: ray
 * @date: 2021/9/13
 */
@Data
@Accessors(chain = true)
public class TaskAreaDTO  implements Serializable {


    private static final long serialVersionUID = -5036967412088251164L;
    private String code;

    private List<String> children;

}