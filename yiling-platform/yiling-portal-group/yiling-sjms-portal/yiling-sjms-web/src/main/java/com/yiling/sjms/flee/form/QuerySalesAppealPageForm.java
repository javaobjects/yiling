package com.yiling.sjms.flee.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/3/11 0011
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuerySalesAppealPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "申报编号")
    private String code;

    @ApiModelProperty(value = "申诉类型 1补传月流向、2调整月流向、3代表终端对应错误、4终端类型申诉、5其他")
    private Integer appealType;

    @ApiModelProperty(value = "状态 ：字典gb_form_status")
    private Integer status;

    @ApiModelProperty(value = "formId")
    private Long formId;
}
