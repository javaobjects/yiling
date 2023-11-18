package com.yiling.b2b.app.shop.vo;

import java.math.BigDecimal;

import com.yiling.common.web.goods.vo.GoodsPriceVO;
import com.yiling.common.web.goods.vo.PresaleInfoVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * B2B-店铺列表的商品信息 VO
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/19
 */
@Data
@ApiModel
public class ShopListItemGoodsVO {

    /**
     * 商品ID
     */
    @ApiModelProperty("商品ID")
    private Long goodsId;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String goodsName;

    /**
     * 企业名称（店铺名称）
     */
    @ApiModelProperty("企业名称（店铺名称）")
    private String shopName;

    /**
     * 商品图片
     */
    @ApiModelProperty("商品图片")
    private String pic;

    /**
     * 商品价格
     */
    @ApiModelProperty("商品价格")
    private GoodsPriceVO goodsPriceVO;

    /**
     * 商品价格
     */
    @ApiModelProperty("商品价格")
    @Deprecated
    private BigDecimal price;

    /**
     * 商品规格
     */
    @ApiModelProperty("商品规格")
    private String specifications;

    /**
     * 是否有秒杀活动
     */
    @ApiModelProperty(value = "是否有秒杀活动")
    private Boolean hasSecKill;

    /**
     * 是否有特价活动
     */
    @ApiModelProperty(value = "是否有特价活动")
    private Boolean hasSpecial;

    /**
     * 商品预售信息
     */
    @ApiModelProperty("商品预售信息")
    private PresaleInfoVO presaleInfoVO;

}
