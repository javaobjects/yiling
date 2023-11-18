package com.yiling.sjms.manor.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询医院辖区变更表单
 * @author: gxl
 * @date: 2023/5/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryChangePageForm extends QueryPageListForm {
    /**
     * form表主键
     */
    @NotNull
    private Long formId;

}