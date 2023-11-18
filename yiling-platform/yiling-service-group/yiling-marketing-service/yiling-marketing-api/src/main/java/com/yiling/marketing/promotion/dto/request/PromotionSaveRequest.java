package com.yiling.marketing.promotion.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PromotionSaveRequest extends BaseRequest {

    /**
     * 活动id
     */
    private Long                                      id;

    /**
     * 促销活动信息
     */
    private PromotionActivitySaveRequest              activity;

    /**
     * 特价秒杀信息
     */
    private PromotionSecKillSpecialRequest            secKillSpecial;

    /**
     * 组合包信息
     */
    private PromotionCombinationPackageRequest  combinationPackage;

    /**
     * 促销活动企业
     */
    private List<PromotionEnterpriseLimitSaveRequest> enterpriseLimitList;

    /**
     * 促销-商品
     */
    private List<PromotionGoodsLimitSaveRequest>      goodsLimitList;

    /**
     * 促销-赠品
     */
    private List<PromotionGoodsGiftLimitSaveRequest>  goodsGiftLimit;

    /**
     * 赠品名称
     */
    private String                                    goodsGiftName;
}
