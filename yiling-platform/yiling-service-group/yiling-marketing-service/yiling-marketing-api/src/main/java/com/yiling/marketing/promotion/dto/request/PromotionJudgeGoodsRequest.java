package com.yiling.marketing.promotion.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionJudgeGoodsRequest extends BaseRequest {

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品总额
     */
    private BigDecimal amount;

    /**
     * 商品数量
     */
    private Integer count;
}
