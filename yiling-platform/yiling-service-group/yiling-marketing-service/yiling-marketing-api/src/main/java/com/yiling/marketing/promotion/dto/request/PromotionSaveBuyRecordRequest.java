package com.yiling.marketing.promotion.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 促销活动购买记录请求参数
 *
 * @author fan.shen
 * @date 2022-2-8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PromotionSaveBuyRecordRequest extends BaseRequest {

    /**
     * 促销活动购买记录列表
     */
    private List<PromotionBuyRecord> buyRecordList;
}
