package com.yiling.order.order.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *收货信息
 * @author:wei.wang
 * @date:2021/6/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrderReceiveListInfoRequest extends BaseRequest {
    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 回执单号
     */
    private List<String> receiveReceiptList;

    /**
     * 商品信息
     */
    private List<SaveOrderReceiveInfoRequest> orderReceiveGoodsInfoList;
}
