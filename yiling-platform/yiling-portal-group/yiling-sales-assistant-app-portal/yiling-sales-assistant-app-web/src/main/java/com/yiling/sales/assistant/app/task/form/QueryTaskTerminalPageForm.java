package com.yiling.sales.assistant.app.task.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 选择终端入参
 * @author: ray
 * @date: 2021/9/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryTaskTerminalPageForm extends QueryPageListForm {


    /**
     * 企业名称或联系电话
     */
    private String nameOrPhone;



    /**
     * 任务id
     */
   private Long taskId;
}