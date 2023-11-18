package com.yiling.f2b.web.mall.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/6/16
 */
@Data
public class GoodsHotWordsVO {

    /**
     * 商品ID
     */
    //@ApiModelProperty(value = "商品ID")
    //private Long goodsId;

    /**
     * 热词名称
     */
    @ApiModelProperty(value = "热词名称")
    private String name;

}
