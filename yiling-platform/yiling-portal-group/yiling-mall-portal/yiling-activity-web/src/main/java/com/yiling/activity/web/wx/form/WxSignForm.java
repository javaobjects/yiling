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
 * @date: 2022/3/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class WxSignForm extends BaseForm {

    @NotNull(message = "签名url不能为空")
    @ApiModelProperty(value = "签名url", required = true)
    private String url;

    @ApiModelProperty(value = "签名类型:例如（jsapi,wx_card）")
    private String type;
}
