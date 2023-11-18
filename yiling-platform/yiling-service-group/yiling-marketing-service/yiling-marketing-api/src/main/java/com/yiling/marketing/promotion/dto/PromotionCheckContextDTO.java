package com.yiling.marketing.promotion.dto;

import java.util.List;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.member.dto.CurrentMemberDTO;
import com.yiling.user.member.dto.request.CurrentMemberForMarketingDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 促销活动-校验上下文
 * </p>
 *
 * @author: fan.shen
 * @date: 2022/1/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromotionCheckContextDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 促销商品
     */
    private List<PromotionGoodsLimitDTO> targetGoodsList;

    /**
     * 促销商品
     */
    private PromotionGoodsLimitDTO currentGoodsLimitDTO;

    /**
     * 促销秒杀特价
     */
    private PromotionSecKillSpecialDTO secKillSpecialDTO;

    /**
     * 企业
     */
    private EnterpriseDTO enterprise;

    /**
     * 会员
     */
    private CurrentMemberForMarketingDTO member;

    /**
     * 买家eid
     */
    private Long buyerEid;

    /**
     * 营销活动id
     */
    private Long promotionActivityId;

    boolean check(){
        return true;
    }

}
