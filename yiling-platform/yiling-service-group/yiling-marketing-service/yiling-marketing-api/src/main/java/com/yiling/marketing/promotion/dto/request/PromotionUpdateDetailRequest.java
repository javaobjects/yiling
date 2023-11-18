package com.yiling.marketing.promotion.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 促销活动购买记录更新参数
 *
 * @author fan.shen
 * @date 2022-2-8
 */
@Data
@Accessors(chain = true)
public class PromotionUpdateDetailRequest extends BaseRequest {

    /**
     * 商品列表
     */
    private Long    goodsId;

    /**
     * 退货数量
     */
    private Integer quantity;
}

