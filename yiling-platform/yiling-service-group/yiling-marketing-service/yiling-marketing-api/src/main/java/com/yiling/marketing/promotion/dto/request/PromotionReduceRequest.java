package com.yiling.marketing.promotion.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.marketing.promotion.dto.PromotionReduceStockDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PromotionReduceRequest extends BaseRequest {

    /**
     * 扣减库存list
     */
    private List<PromotionReduceStockDTO> reduceStockList;

}
