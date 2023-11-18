package com.yiling.pricing.goods.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 QueryGoodsLimitPriceRequest
 * @描述
 * @创建时间 2023/1/4
 * @修改人 shichen
 * @修改时间 2023/1/4
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryPopLimitPriceRequest extends BaseRequest {
    private List<Long> sellSpecificationsIds;
    private Integer status;
}
