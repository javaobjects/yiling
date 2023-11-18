package com.yiling.sales.assistant.task.dto.request;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: ray
 * @date: 2021/9/14
 */
@Data
@Accessors(chain = true)
public class UpdateTaskAreaRequest  implements Serializable {

    private static final long serialVersionUID = 8467709617988869179L;
    private Integer code;

    private List<Integer> selected;

    private List<UpdateTaskAreaRequest> children;

    private String name;
}