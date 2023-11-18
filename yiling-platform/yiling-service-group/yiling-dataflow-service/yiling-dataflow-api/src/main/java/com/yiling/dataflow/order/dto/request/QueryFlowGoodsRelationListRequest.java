package com.yiling.dataflow.order.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/9/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowGoodsRelationListRequest extends BaseRequest {

    /**
     * 商业公司ID
     */
    private Long eid;

    /**
     * 商品内码
     */
    private String goodsInSn;

    /**
     * 以岭品id
     */
    private Long ylGoodsId;
}
