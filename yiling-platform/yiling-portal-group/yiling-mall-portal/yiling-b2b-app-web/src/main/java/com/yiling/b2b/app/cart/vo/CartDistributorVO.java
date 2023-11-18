package com.yiling.b2b.app.cart.vo;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 购物车列表项 VO
 *
 * @author: xuan.zhou
 * @date: 2021/6/21
 */
@Data
public class CartDistributorVO {

    @ApiModelProperty("配送商企业ID")
    private Long distributorEid;

    @ApiModelProperty("配送商名称")
    private String distributorName;

    @ApiModelProperty("商品列表")
    private List<CartGoodsVO> cartGoodsList;

    @ApiModelProperty("已选择商品数")
    private Long selectedGoodsNum;

    @ApiModelProperty("已选择商品总金额")
    private BigDecimal selectedGoodsAmount;

    @ApiModelProperty("是否能勾选")
    private Boolean selectEnabled;

    @ApiModelProperty("是否选中")
    private Boolean selected;

    @ApiModelProperty("是否有赠品")
    private Boolean hasGift;

    @ApiModelProperty("起配金额")
    private BigDecimal startPassAmount;

    @ApiModelProperty("起配剩余金额")
    private BigDecimal passRemainAmount;

    @ApiModelProperty("是否满足店铺配送金额")
    private Boolean isMatchShopDistribute;

    @ApiModelProperty("赠品活动")
    private List<PromotionActivity> promotionActivityList;

    @Data
    public static class PromotionActivity {

        @ApiModelProperty("活动ID")
        private Long activityId;
        @ApiModelProperty("赠品名称")
        private String  giftName;
        @ApiModelProperty("赠品数量")
        private Integer giftNum;
        @ApiModelProperty("赠品图片")
        private String pictureUrl;
        @ApiModelProperty("满赠金额")
        private BigDecimal giftAmountLimit;
        @ApiModelProperty("相差金额")
        private BigDecimal passRemainGifAmount;
        @ApiModelProperty("是否满足赠品赠送条件")
        private Boolean isMatchGift;
        @ApiModelProperty("满足赠品活动的商品集合")
        private  List<Long> activityGoodList;
    }
}
