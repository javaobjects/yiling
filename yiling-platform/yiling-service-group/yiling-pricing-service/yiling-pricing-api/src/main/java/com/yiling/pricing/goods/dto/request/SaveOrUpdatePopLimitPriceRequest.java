package com.yiling.pricing.goods.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 SaveOrUpdatePopLimitPriceRequest
 * @描述
 * @创建时间 2023/1/4
 * @修改人 shichen
 * @修改时间 2023/1/4
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdatePopLimitPriceRequest extends BaseRequest {
    private Long id;

    /**
     * 标准库ID
     */
    private Long standardId;

    /**
     * 标准库售卖规格ID
     */
    private Long  sellSpecificationsId;

    /**
     * 价格上限
     */
    private BigDecimal upperLimitPrice;

    /**
     * 价格下限
     */
    private BigDecimal lowerLimitPrice;

    /**
     * 状态 0：正常
     */
    private Integer status;

}
