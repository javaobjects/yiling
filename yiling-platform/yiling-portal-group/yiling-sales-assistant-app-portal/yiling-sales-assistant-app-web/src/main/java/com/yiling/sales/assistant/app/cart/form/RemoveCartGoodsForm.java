package com.yiling.sales.assistant.app.cart.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 移除购物车商品 Form
 *
 * @author: xuan.zhou
 * @date: 2021/6/21
 */
@Data
public class RemoveCartGoodsForm {

    @NotEmpty
    @ApiModelProperty(value = "购物车ID列表", required = true)
    private List<Long> ids;

    @NotNull
    @ApiModelProperty(value = "选择的客户", required = true)
    private Long customerEid;
}
