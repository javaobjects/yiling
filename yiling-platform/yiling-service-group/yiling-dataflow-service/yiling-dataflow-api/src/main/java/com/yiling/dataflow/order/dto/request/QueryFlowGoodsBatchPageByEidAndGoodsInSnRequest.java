package com.yiling.dataflow.order.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/6/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowGoodsBatchPageByEidAndGoodsInSnRequest extends QueryPageListRequest {

    /**
     * 商业id
     */
    private Long eid;

    /**
     * 商品内码
     */
    private String goodsInSn;

}
