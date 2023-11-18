package com.yiling.f2b.web.cart.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 勾选购物车商品 Form
 *
 * @author: xuan.zhou
 * @date: 2021/6/21
 */
@Data
public class SelectCartGoodsForm {

    @NotEmpty
    @ApiModelProperty(value = "购物车ID列表", required = true)
    private List<Long> ids;

    @NotNull
    @ApiModelProperty(value = "是否勾选", required = true)
    private Boolean selected;
}
