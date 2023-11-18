package com.yiling.admin.b2b.promotion.vo;

import java.util.List;

import com.yiling.admin.b2b.goods.vo.GoodsListItemVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 促销活动主表
 *
 * @author: yong.zhang
 * @date: 2021/11/2
 */
@Data
@Accessors(chain = true)
public class PromotionVO {

    @ApiModelProperty(value = "促销活动")
    private PromotionActivityVO promotionActivity;

    @ApiModelProperty(value = "促销活动-秒杀&特价")
    private PromotionSecKillSpecialVO promotionSecKillSpecial;

    @ApiModelProperty(value = "促销活动企业")
    private List<PromotionEnterpriseLimitVO> promotionEnterpriseLimitList;

    @ApiModelProperty(value = "促销活动商品")
    private List<GoodsListItemVO> promotionGoodsLimitList;

    @ApiModelProperty(value = "促销活动赠品")
    private List<PromotionGoodsGiftLimitVO> promotionGoodsGiftLimitList;

    @ApiModelProperty(value = "促销活动-组合包")
    private PromotionCombinationPackageVO promotionCombinationPackage;


}
