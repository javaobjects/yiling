package com.yiling.admin.data.center.report.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-05-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RejectReportForm extends BaseForm {

    /**
     * 报表id
     */
    @NotNull
    @ApiModelProperty("报表id")
    private Long reportId;

    /**
     * 驳回类型 1-运营驳回 2-财务驳回 3-管理员驳回
     */
    @Range(min = 1,max = 3)
    @ApiModelProperty( "驳回类型 1-运营驳回 2-财务驳回 3-管理员驳回")
    private Integer rejectType;

    /**
     * 驳回原因
     */
    @NotBlank
    @ApiModelProperty("驳回原因")
    private String rejectReason;


}
