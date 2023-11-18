package com.yiling.admin.data.center.report.form;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-08-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RebateForm extends BaseForm {

    /**
     * 报表id
     */
    @NotNull
    @ApiModelProperty("报表id")
    private Long reportId;

    /**
     * 报表明细id列表
     */
    @NotNull
    @ApiModelProperty("报表明细id列表")
    private List<Long> detailIdList;
}
