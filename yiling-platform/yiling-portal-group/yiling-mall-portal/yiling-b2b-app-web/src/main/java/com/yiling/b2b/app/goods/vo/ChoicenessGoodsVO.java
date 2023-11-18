package com.yiling.b2b.app.goods.vo;

import java.util.Date;
import java.util.List;

import com.yiling.b2b.app.promotion.vo.CombinationPackageVO;
import com.yiling.b2b.app.shop.vo.ShopDetailVO;
import com.yiling.common.web.goods.vo.GoodsItemVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author: shuang.zhang
 * @date: 2021/11/11
 */
@Getter
@Setter
@ToString
public class ChoicenessGoodsVO extends GoodsItemVO {

    /**
     * 商家信息
     */
    @ApiModelProperty(value = "商家信息")
    private ShopDetailVO     shopDetailVO;

    /**
     * 所属省份名称
     */
    @ApiModelProperty("所属省份名称")
    private String           provinceName;

    /**
     * 所属城市名称
     */
    @ApiModelProperty("所属城市名称")
    private String           cityName;

    /**
     * 所属区域名称
     */
    @ApiModelProperty("所属区域名称")
    private String           regionName;

    /**
     * 有效期
     */
    @ApiModelProperty("有效期")
    private Date expiryDate;

    /**
     * 销售包装商品信息
     */
    @ApiModelProperty(value = "销售包装商品信息")
    private List<GoodsSkuVO> goodsSkuList;

    /**
     * 是否有满折卷活动
     */
    @ApiModelProperty(value = "是否有满折卷活动")
    private Boolean          isDiscountCoupon = false ;

    /**
     * 是否有满减卷活动
     */
    @ApiModelProperty(value = "是否有满减卷活动")
    private Boolean          isReduceCoupon = false;

    /**
     * 是否有满赠活动
     */
    @ApiModelProperty(value = "是否有满赠活动")
    private Boolean          isGiftActivity = false;

    /**
     * 是否有秒杀活动
     */
    @ApiModelProperty(value = "是否有秒杀活动")
    private Boolean          hasSecKill     = false;

    /**
     * 是否有特价活动
     */
    @ApiModelProperty(value = "是否有特价活动")
    private Boolean          hasSpecial     = false;

    @ApiModelProperty(value = "是否有策略满赠活动")
    private Boolean hasStrategyActivity     = false;

    @ApiModelProperty(value = "是否有库存")
    private Boolean hasStock = false;

    @ApiModelProperty(value = "组合包活动列表")
    private List<CombinationPackageVO> combinationPackageList;
}
