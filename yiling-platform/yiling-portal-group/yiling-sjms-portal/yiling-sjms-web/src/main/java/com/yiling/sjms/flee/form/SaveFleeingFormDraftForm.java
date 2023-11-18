package com.yiling.sjms.flee.form;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.sjms.flee.vo.AppendixDetailVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2023/3/15 0015
 */
@Data
public class SaveFleeingFormDraftForm extends BaseForm {

    /**
     * 主流程表单id
     */
    @NotNull(message = "上传的文件不能为空")
    @ApiModelProperty("主流程表单id")
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
    private List<AppendixDetailVO> appendixList;
}
