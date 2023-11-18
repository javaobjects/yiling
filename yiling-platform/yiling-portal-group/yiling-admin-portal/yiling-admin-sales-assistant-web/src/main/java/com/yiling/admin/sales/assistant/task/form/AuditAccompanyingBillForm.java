package com.yiling.admin.sales.assistant.task.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2023/1/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AuditAccompanyingBillForm extends BaseForm {
    @ApiModelProperty(value = "随货同行单id",required = true)
    private Long id;
    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态  1-审核通过 3-驳回",required = true)
    private Integer auditStatus;

    /**
     * 驳回原因
     */
    @ApiModelProperty(value = "驳回原因")
    private String rejectionReason;
}