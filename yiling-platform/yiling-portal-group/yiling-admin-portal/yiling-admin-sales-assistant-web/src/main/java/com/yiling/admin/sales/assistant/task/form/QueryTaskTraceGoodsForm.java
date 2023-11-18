package com.yiling.admin.sales.assistant.task.form;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 任务追踪-任务品完成进度查询参数
 * @author gaoxinlei
 */
@Data
public class QueryTaskTraceGoodsForm implements Serializable {


    private static final long serialVersionUID = -1369108555166784729L;
    @ApiModelProperty(value = "用户任务主键")
    @NotNull
    private Long userTaskId;
}
