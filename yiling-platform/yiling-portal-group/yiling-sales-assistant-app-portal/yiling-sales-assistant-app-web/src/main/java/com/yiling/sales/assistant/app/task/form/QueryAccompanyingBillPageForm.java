package com.yiling.sales.assistant.app.task.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 随货同行单列表查询参数
 * @author: gxl
 * @date: 2023/1/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAccompanyingBillPageForm extends QueryPageListForm {


    /**
     * 单据编号
     */
    @ApiModelProperty(value = "单据编号")
    private String docCode;

    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态 1-审核中 2-驳回 3-通过",required = true)
    private Integer auditStatus;


}