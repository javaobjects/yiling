package com.yiling.settlement.b2b.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date: 2022/04/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateOrderSyncSettStatusRequest extends BaseRequest {

    /**
     * 修改结算状态集合
     */
    private List<OrderSettlementStatusRequest> settStatusList;

    @Data
    @EqualsAndHashCode(callSuper = true)
    @Accessors(chain = true)
    public  static class  OrderSettlementStatusRequest extends BaseRequest{

        /**
         * 同步表id
         */
        private Long id;

        /**
         * 是否生成结算单(1-未生成,2-已生成)
         */
        private Integer generateStatus;

        /**
         * 货款结算状态(1-未结算,2-已结算)
         */
        private Integer goodsSettlementStatus;

        /**
         * 促销结算状态(1-未结算,2-已结算)
         */
        private Integer saleSettlementStatus;

        /**
         * 促销结算状态(1-未结算,2-已结算)
         */
        private Integer presaleSettlementStatus;
    }
}
