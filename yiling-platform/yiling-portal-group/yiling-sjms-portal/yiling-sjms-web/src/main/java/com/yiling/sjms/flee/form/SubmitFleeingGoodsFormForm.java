package com.yiling.sjms.flee.form;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2023/2/25 0025
 */
@Data
public class SubmitFleeingGoodsFormForm extends BaseForm {

    @ApiModelProperty("父表单id")
    private Long formId;

    /**
     * 申报类型 1-电商 2-非电商
     */
    @ApiModelProperty("申报类型 1-电商 2-非电商")
    private Integer reportType;

    /**
     * 申诉描述
     */
    @ApiModelProperty(value = "申诉描述")
    private String describe;

    /**
     * 附件
     */
    @ApiModelProperty(value = "附件")
    private List<AppendixDetailForm> appendixList;
}
