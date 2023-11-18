package com.yiling.f2b.web.mall.vo;

import java.util.List;

import com.yiling.common.web.goods.vo.GoodsPriceVO;
import com.yiling.f2b.web.goods.vo.DistributorGoodsVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/6/29
 */
@Data
public class QuickPurchaseGoodsVO {
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
     * 单位
     */
    @ApiModelProperty("单位")
    private String sellUnit;

    /**
     * 配送商Id
     */
    @ApiModelProperty("配送商Id")
    private Long distributorEid;

    /**
     * 商品价格信息
     */
    @ApiModelProperty("商品价格信息")
    private GoodsPriceVO priceInfo;

    /**
     * 配送商个数
     */
    @ApiModelProperty("配送商个数")
    private Integer distributorCount;

    @ApiModelProperty("配送商商品列表")
    private List<DistributorGoodsVO> distributorGoodsList;

    /**
     * 超卖type
     */
    @ApiModelProperty("超卖type 0-正常商品  1-超卖商品")
    private Integer overSoldType;



}
