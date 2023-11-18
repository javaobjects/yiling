package com.yiling.common.web.cart.vo;

import com.yiling.mall.agreement.enums.GoodsLimitStatusEnum;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 加入购物车按钮 VO
 *
 * @author: xuan.zhou
 * @date: 2021/6/15
 */
@Data
public class AddToCartButtonVO {

    public AddToCartButtonVO(GoodsLimitStatusEnum goodsLimitStatusEnum,Boolean enabled) {
        this.text = goodsLimitStatusEnum.getName();
        this.goodsLimitStatus = goodsLimitStatusEnum.getCode();
        this.enabled = enabled;
    }

    /**
     * 按钮文本
     */
    @ApiModelProperty("按钮文本")
    private String text;

    /**
     * 是否可用
     */
    @ApiModelProperty("是否可用")
    private Boolean enabled;



    private Integer goodsLimitStatus;

}
