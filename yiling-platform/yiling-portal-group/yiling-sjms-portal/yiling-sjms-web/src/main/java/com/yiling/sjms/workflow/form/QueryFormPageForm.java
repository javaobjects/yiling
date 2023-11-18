package com.yiling.sjms.workflow.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询流程列表参数
 * @author: gxl
 * @date: 2023/2/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFormPageForm extends QueryPageListForm {
    /**
     * 单据编号
     */
    @ApiModelProperty(value = "单据编号")
    private String code;

    /**
     * 单据类型 form_type
     */
    @ApiModelProperty(value = "业务流程 字典form_type")
    private Integer type;

    /**
     * 状态：10-待提交 20-审批中 200-已通过 201-已驳回
     */
    @ApiModelProperty(value = "状态 ：字典gb_form_status")
    private Integer status;
}