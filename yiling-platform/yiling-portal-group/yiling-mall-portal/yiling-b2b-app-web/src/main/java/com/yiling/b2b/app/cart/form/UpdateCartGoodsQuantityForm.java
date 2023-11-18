package com.yiling.b2b.app.cart.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 修改购物车商品数量 Form
 *
 * @author: xuan.zhou
 * @date: 2021/6/21
 */
@Data
public class UpdateCartGoodsQuantityForm {

    @NotNull
    @ApiModelProperty(value = "购物车ID", required = true)
    private Long id;

    @NotNull
    @Range(min = 1)
    @ApiModelProperty(value = "数量", required = true)
    private Integer quantity;

}
