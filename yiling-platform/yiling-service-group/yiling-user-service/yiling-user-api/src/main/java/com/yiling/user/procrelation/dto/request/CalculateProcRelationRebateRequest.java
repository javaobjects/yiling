package com.yiling.user.procrelation.dto.request;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2021/7/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CalculateProcRelationRebateRequest extends BaseRequest {

    /**
     * 买方Id
     */
    private Long buyerEid;

    /**
     * 卖方Id
     */
    private Long sellerEid;

    /**
     * 购买商品明细
     */
    private List<CalculateProcRelationGoodsRequest> goodsList;

    /**
     * 配送商订单 Request
     */
    @Data
    public static class CalculateProcRelationGoodsRequest implements java.io.Serializable {

        /**
         * 订单商品Id
         */
        private Long goodsId;

        /**
         * 商品购买数量
         */
        private Long goodsQuantity;

        /**
         * 订单商品购买总价
         */
        private BigDecimal goodsAmount;

    }

}
