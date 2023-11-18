package com.yiling.dataflow.order.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/6/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteFlowGoodsRelationRequest extends BaseRequest {

    /**
     * 商业公司eid
     */
    private Long eid;

    /**
     * 商业商品内码
     */
    private String goodsInSn;

}
