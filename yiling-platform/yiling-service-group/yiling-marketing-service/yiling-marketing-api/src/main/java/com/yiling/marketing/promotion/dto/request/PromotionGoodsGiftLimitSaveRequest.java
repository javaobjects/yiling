package com.yiling.marketing.promotion.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/4
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionGoodsGiftLimitSaveRequest extends BaseRequest implements Serializable {

    /**
     * 赠送商品ID
     */
    private Long       goodsGiftId;

    /**
     * 满赠金额
     */
    private BigDecimal promotionAmount;

    /**
     * 活动库存
     */
    private Integer    promotionStock;

}
