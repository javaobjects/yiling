package com.yiling.admin.sales.assistant.task.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 任务区域
 * @author: ray
 * @date: 2021/9/13
 */
@Data
@Accessors(chain = true)
public class TaskAreaVO  {

    @ApiModelProperty(value = "父区域code")
    private String code;
    @ApiModelProperty(value = "子区域code集合")
    private List<String> children;

}