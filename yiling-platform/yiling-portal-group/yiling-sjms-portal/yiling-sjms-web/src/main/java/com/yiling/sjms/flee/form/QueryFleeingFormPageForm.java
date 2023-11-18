package com.yiling.sjms.flee.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2023/3/11 0011
 */
@Data
public class QueryFleeingFormPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "申报编号")
    private String code;

    @ApiModelProperty(value = "申报类型 1-电商、2-非电商")
    private Integer reportType;

    @ApiModelProperty(value = "状态 ：字典gb_form_status")
    private Integer status;

    /**
     * 确认状态：1-待确认 2-生成中 3-生成成功 4-生成失败
     */
    @ApiModelProperty(value = "确认状态：1-待确认 2-生成中 3-生成成功 4-生成失败")
    private Integer confirmStatus;
}
