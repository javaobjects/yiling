package com.yiling.admin.data.center.report.form;

import java.math.BigDecimal;

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
public class ReportConfirmForm extends BaseForm {

    /**
     * 报表id
     */
    @NotNull
    @ApiModelProperty("报表id")
    private Long reportId;

    /**
     * 确认类型 1-运营确认 2-财务确认
     */
    @Range(min = 1,max = 2)
    @ApiModelProperty( "确认类型 1-运营确认 2-财务确认")
    private Integer type;

}
