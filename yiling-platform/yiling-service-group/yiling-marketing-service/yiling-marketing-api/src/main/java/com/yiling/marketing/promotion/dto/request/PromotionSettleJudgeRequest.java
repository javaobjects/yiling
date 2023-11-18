package com.yiling.marketing.promotion.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 结算校验满赠参数
 *
 * @author: fan.shen
 * @date: 2022/2/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PromotionSettleJudgeRequest extends BaseRequest {

    /**
     * 活动列表
     */
    private List<PromotionSettleJudgeRequestItem> requestItemList;

    /**
     * 订单明细信息
     */
    private List<PromotionJudgeGoodsRequest> goodsRequestList;

}

