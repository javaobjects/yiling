package com.yiling.sjms.flee.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2023/3/15 0015
 */
@Data
public class SaveConfirmFleeingGoodsForm extends BaseForm {

    /**
     * 主流程表单id
     */
    @NotNull
    @ApiModelProperty("主流程表单id")
    private Long formId;

    /**
     * 文件Key
     */
    @NotEmpty
    @ApiModelProperty(value = "文件Key", required = true)
    private String fileKey;

    /**
     * 文件名
     */
    @NotEmpty
    @ApiModelProperty(value = "文件名", required = true)
    private String fileName;
}
