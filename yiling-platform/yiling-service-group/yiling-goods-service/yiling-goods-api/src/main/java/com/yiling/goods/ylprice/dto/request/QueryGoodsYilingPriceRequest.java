package com.yiling.goods.ylprice.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/8/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGoodsYilingPriceRequest extends BaseRequest {

    /**
     * 规格id
     */
    private List<Long> specificationIdList;

    /**
     * 时间
     */
    private List<Date> timeList;

}
