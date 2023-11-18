package com.yiling.mall.agreement.dto.request;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/7/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementCalculateRequest extends BaseRequest {

    /**
     * 采购商Id
     */
    private Long buyerEid;

    /**
     * 配送商Id
     */
    private Long distributorEid;

    /**
     * 支付方式
     */
    private Integer paymentMethod;

    /**
     * 购买商品明细
     */
    private List<AgreementCalculateDetailRequest> agreementCalculateDetailList;

    /**
     * 配送商订单 Request
     */
    @Data
    public static class AgreementCalculateDetailRequest implements java.io.Serializable {

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
