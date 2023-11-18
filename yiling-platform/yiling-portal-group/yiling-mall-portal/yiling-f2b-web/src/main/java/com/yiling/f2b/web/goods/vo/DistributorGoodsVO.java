package com.yiling.f2b.web.goods.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.common.web.goods.vo.GoodsPriceVO;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-06-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DistributorGoodsVO extends BaseVO {

    /**
     * 销售包装商品信息
     */
    @ApiModelProperty(value = "销售包装商品信息")
    private List<GoodsSkuVO> goodsSkuList;

    /**
     * 配送商id
     */
    @ApiModelProperty("配送商id")
    private Long distributorId;

    /**
     * 配送商名称
     */
    @ApiModelProperty("配送商名称")
    private String distributorName;

    /**
     * 商品id
     */
    @ApiModelProperty("商品id")
    private Long goodsId;

    /**
     * 以岭商品Id
     */
    @ApiModelProperty("以岭商品Id")
    private Long ylGoodsId;

    /**
     * 以岭商品商品名称
     */
    @ApiModelProperty("以岭商品商品名称")
    private String name;

    /**
     * 商品价格信息
     */
    @ApiModelProperty("商品价格信息")
    private GoodsPriceVO priceInfo;

    /**
     * 规格
     */
    @ApiModelProperty("规格")
    private String sellSpecifications;

    /**
     * 超卖type
     */
    @ApiModelProperty("超卖type 0-正常商品  1-超卖商品")
    private Integer overSoldType;

    /**
     * 商品优化折扣，单位为百分比
     */
    @ApiModelProperty("商品优化折扣，单位为百分比")
    private BigDecimal rebate;


}
