package com.yiling.sales.assistant.app.order.form;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.sales.assistant.app.order.form
 * @date: 2021/10/9
 */
@Data
@Accessors(chain = true)
public class ShareOrderProductInfoForm {

    @ApiModelProperty(value = "分享订单转发生成的字符串")
    @NotNull
    private String keyStr;

}
