package com.yiling.marketing.promotion.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 促销活动购买记录更新参数
 *
 * @author fan.shen
 * @date 2022-2-8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PromotionUpdateBuyRecordRequest extends BaseRequest {

    /**
     * 订单id
     */
    private Long         orderId;

    /**
     * 商品列表
     */
    private List<PromotionUpdateDetailRequest> detailList;

}
