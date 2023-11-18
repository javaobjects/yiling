package com.yiling.b2b.app.cart.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 组合优惠添加
 * @author zhigang.guo
 * @date: 2022/4/25
 */
@Data
public class CombinationAddCartForm {

    @NotNull
    @Min(1L)
    @ApiModelProperty(value = "组合优惠活动ID", required = true)
    private Long promotionActivityId;

    @NotNull
    @Min(1)
    @ApiModelProperty(value = "购买数量", required = true)
    private Integer quantity;

    @NotNull
    @Min(1L)
    @ApiModelProperty(value = "卖家企业ID", required = true)
    private Long distributorEid;


}
