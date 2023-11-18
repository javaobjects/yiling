package com.yiling.sjms.gb.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/5/23
 */
@Data
public class SaveGbOrderForm extends BaseForm {

    /**
     * 团购表单ID列表
     */
    @NotEmpty
    @ApiModelProperty(value = "团购表单ID列表")
    private List<Long> formIdList;

}
