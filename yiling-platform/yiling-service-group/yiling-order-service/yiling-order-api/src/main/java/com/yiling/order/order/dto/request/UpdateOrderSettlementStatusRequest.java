package com.yiling.order.order.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.order.order.dto.request
 * @date: 2021/10/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateOrderSettlementStatusRequest  extends BaseRequest {

    /**
     * 修改结算状态集合
     */
    private List<OrderSettlementStatusRequest> orderSettlementStatusRequestList;

    @Data
    @EqualsAndHashCode(callSuper = true)
    @Accessors(chain = true)
    public  static class  OrderSettlementStatusRequest extends BaseRequest{

        private Long orderId;

        /**
         * 是否生成结算单(1-未生成,2-已生成)
         */
        private Integer isSettlementOrder;

        /**
         * 货款结算状态(1-未结算,2-已结算)
         */
        private Integer goodsSettlementStatus;
        /**
         * 促销结算状态(1-未结算,2-已结算)
         */
        private Integer saleSettlementStatus;
    }
}
