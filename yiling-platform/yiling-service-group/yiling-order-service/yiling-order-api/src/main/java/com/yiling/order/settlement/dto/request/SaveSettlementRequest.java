package com.yiling.order.settlement.dto.request;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/8/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveSettlementRequest extends BaseRequest {
    /**
     * 订单ID
     */
    private String erpReceivableNo;

    private Date deliveryTime;

    /**
     * 订单Id
     */
    private Long orderId;

    private Long sellerEid;

    private Long buyerEid;

    private BigDecimal settlementAmount;

    /**
     * 回款核销金额
     */
    private BigDecimal backAmount;

    /**
     * 红票核销金额
     */
    private BigDecimal discountsAmount;

    private Integer backAmountType;


    private List<SaveSettlementDetailRequest> saveSettlementDetailRequestList;


    /**
     * 配送商订单 Request
     */
    @Data
    public static class SaveSettlementDetailRequest implements java.io.Serializable {

        /**
         * 订单Id
         */
        private Long   orderId;
        /**
         * 订单ID
         */
        private String erpReceivableNo;

        private Long sellerEid;

        private Long buyerEid;

        /**
         * 商品Id
         */
        private Long goodsId;

        /**
         * 商品Id
         */
        private Long orderDetailId;

        /**
         * 以岭商品Id
         */
        private String goodsInSn;

        /**
         * 回款核销金额
         */
        private BigDecimal backAmount;

        /**
         * 回款核销金额
         */
        private BigDecimal settlementAmount;

        /**
         * 红票核销金额
         */
        private BigDecimal discountsAmount;

    }
}
