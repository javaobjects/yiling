package com.yiling.b2b.app.cart.vo;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.framework.common.pojo.vo.SimpleGoodsVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 购物车商品 VO
 *
 * @author: xuan.zhou
 * @date: 2021/6/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CartGoodsVO extends SimpleGoodsVO {

    @ApiModelProperty("购物车ID")
    private Long cartId;

    @ApiModelProperty("配送商商品ID")
    private Long distributorGoodsId;

    @ApiModelProperty("是否能勾选")
    private Boolean selectEnabled;

    @ApiModelProperty("是否选中")
    private Boolean selected;

    @ApiModelProperty("大件装")
    private Integer bigPackage;

    @ApiModelProperty("销售单位")
    private String sellUnit;

    @ApiModelProperty("销售单位")
    private Long packageNumber;

    @ApiModelProperty("库存数量")
    private Long stockNum;

    @ApiModelProperty("单价")
    private BigDecimal price;

    @ApiModelProperty("数量")
    private Integer quantity;

    @ApiModelProperty("金额")
    private BigDecimal amount;

    @ApiModelProperty("库存描述")
    private String stockText;

    @ApiModelProperty("状态：状态：1-正常 2-商品,3-无货,4-失效")
    private Integer status;

    @ApiModelProperty("状态描述")
    private String statusText;

    @ApiModelProperty("描述")
    private String remark;

    @ApiModelProperty("组合包名称")
    private String packageName;

    @ApiModelProperty("组合包简称")
    private String packageShortName;

    @ApiModelProperty("组合包起订量")
    private Integer pakageInitialNum;

    /**
     *促销活动类型
     * {@link com.yiling.order.order.enums.PromotionActivityTypeEnum}
     */
    @ApiModelProperty("促销活动类型:0,正常商品,2,特价商品,3,秒杀商品,4,组合商品")
    private Integer promotionActivityType;

    @ApiModelProperty("促销活动信息")
    private GoodsPromotionActivity promotionActivity;

    @Data
    @Accessors(chain = true)
    public static class GoodsPromotionActivity extends BaseVO {

        @ApiModelProperty("促销活动剩余商品数量")
        private Integer leftBuyCount;

        @ApiModelProperty("是否容许修改促销数量")
        private Boolean isAllowPromotion;

        @ApiModelProperty("促销活动ID")
        private Long promotionActivityId;

        @ApiModelProperty("促销活动名称")
        private String promotionActivityName;

        @ApiModelProperty("活动提示")
        private String tips;
    }


    /**
     * 组合商品信息
     */
    @Data
    @Accessors(chain = true)
    public static class combinationGoodsVO extends BaseVO {

        @ApiModelProperty("促销活动剩余商品数量")
        private Integer leftBuyCount;

        @ApiModelProperty("是否容许修改促销数量")
        private Boolean isAllowPromotion;

        @ApiModelProperty("促销活动ID")
        private Long promotionActivityId;

        @ApiModelProperty("促销活动名称")
        private String promotionActivityName;

        @ApiModelProperty("活动提示")
        private String tips;
    }
}
