package com.yiling.admin.sales.assistant.task.form;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 任务追踪查询参数
 * @author: ray
 * @date: 2021/9/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryTaskTraceTaskUserForm extends QueryPageListForm {


    @NotNull
    private Long taskId;

    private String name;

    private String mobile;

    private Date startTime;

    private Date endTime;
}