package com.yiling.sjms.flee.form;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shixing.sun
 * @date: 2023/3/13 0013
 */
@Data
public class SaveSalesAppealForm extends BaseForm {

    /**
     * 批量添加请求体
     */
    @ApiModelProperty("批量添加请求体")
    private List<SaveSalesAppealDetailForm> saveSalesAppealDetailForms;

    @ApiModelProperty("formId-不是第一次添加的时候带上")
    private Long formId;

    /**
     * 申诉类型 1补传月流向、2调整月流向、3代表终端对应错误、4终端类型申诉、5其他
     */
    @ApiModelProperty(value = "申诉类型 1补传月流向、2调整月流向、3代表终端对应错误、4终端类型申诉、5其他")
    private Integer appealType;
}
