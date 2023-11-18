package com.yiling.sales.assistant.app.cart.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 草稿箱选择配送商,返回结果
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.sales.assistant.app.cart.vo
 * @date: 2021/9/23
 */
@Data
public class CartChooseCustomerVO {

    @ApiModelProperty("配送商企业ID")
    private List<Long> distributorEids;

    @ApiModelProperty("客户ID")
    private Long cusomterEid;

    @ApiModelProperty("是否跳转购物车")
    private Boolean isSkipCart;
}
