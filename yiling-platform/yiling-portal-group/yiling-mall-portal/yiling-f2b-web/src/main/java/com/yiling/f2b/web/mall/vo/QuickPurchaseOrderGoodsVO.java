package com.yiling.f2b.web.mall.vo;

import com.yiling.common.web.goods.vo.GoodsPriceVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/6/29
 */
@Data
public class QuickPurchaseOrderGoodsVO {
    /**
     * 商品ID
     */
    @ApiModelProperty("配送商品ID")
    private Long id;

    /**
     * 商品ID
     */
    @ApiModelProperty("以岭商品ID")
    private Long ylGoodsId;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String name;

    /**
     * 规格
     */
    @ApiModelProperty("规格")
    private String sellSpecifications;

    /**
     * 配送商名称
     */
    @ApiModelProperty("配送商名称")
    private String ename;

    /**
     * 商品价格信息
     */
    @ApiModelProperty("商品价格信息")
    private GoodsPriceVO priceInfo;



}
