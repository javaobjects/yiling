package com.yiling.sales.assistant.app.task.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 任务追踪--销售记录
 * @author: ray
 * @date: 2021/9/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMyTaskOrderForm extends QueryPageListForm {

    private Long userTaskId;
}