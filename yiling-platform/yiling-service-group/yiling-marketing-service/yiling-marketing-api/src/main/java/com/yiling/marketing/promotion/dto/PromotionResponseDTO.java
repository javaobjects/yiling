package com.yiling.marketing.promotion.dto;

import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

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
public class PromotionResponseDTO extends BaseDTO {

    private PromotionActivityDTO promotionActivityDTO;

    private List<PromotionEnterpriseLimitDTO> enterpriseLimitDTOList;

    private List<PromotionGoodsGiftLimitDTO> goodsGiftLimitDTOList;

    private List<PromotionGoodsLimitDTO> goodsLimitDTOList;
}
