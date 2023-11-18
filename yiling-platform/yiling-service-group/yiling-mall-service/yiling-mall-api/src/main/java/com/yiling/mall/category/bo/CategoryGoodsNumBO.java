package com.yiling.mall.category.bo;

import lombok.Data;

/**
 * 分类商品数量 BO
 *
 * @author: yeucheng.chen
 * @date: 2021/6/18
 */
@Data
public class CategoryGoodsNumBO {

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 商品数量
     */
    private Long goodsNum;
}
