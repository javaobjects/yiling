package com.yiling.activity.web.wx.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @date: 2023/1/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class GetWxAccessTokenForm  extends BaseForm {

    @NotNull(message = "应用不能为空")
    @ApiModelProperty(value = "应用名称", required = true)
    private String app;
}
