package com.yiling.sales.assistant.app.order.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.yiling.framework.common.util.Constants;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/** 获取确认订单验证码
 * @author zhigang.guo
 * @date: 2022/10/20
 */
@Data
public class GetConfirmVerifyCodeForm {

    /**
     * 手机号
     */
    @NotEmpty(message = "^请填写手机号")
    @Pattern(regexp = Constants.REGEXP_MOBILE, message = "^您填写的手机号格式有误，请重新填写")
    @ApiModelProperty(value = "手机号", required = true)
    private String mobile;

    @ApiModelProperty(value = "分享订单转发生成的字符串")
    @NotNull
    private String keyStr;


}
