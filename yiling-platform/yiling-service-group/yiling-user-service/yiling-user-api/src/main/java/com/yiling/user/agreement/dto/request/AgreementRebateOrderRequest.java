package com.yiling.user.agreement.dto.request;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/7/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementRebateOrderRequest extends BaseRequest {

    private static final long serialVersionUID = -5045331341880627054L;

    /**
     * 销售主体Eid
     */
    private Long eid;

    /**
     * 采购方eid
     */
    private Long secondEid;

    /**
     * 账号信息
     */
    private String easAccount;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 退货单Id
     */
    private Long returnId;

    private Long settlementId;

    private Date deliveryTime;

    private Integer type;

    /**
     * 协议Id
     */
    private Long agreementId;

    /**
     * 兑付条件ID
     */
    private Long agreementConditionId;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 商品购买总额
     */
    private BigDecimal goodsAmount;

    /**
     * 购买数量
     */
    private Long goodsQuantity;

    /**
     * 订单返利金额
     */
    private BigDecimal discountAmount;

    /**
     * 支付方式
     */
    private Integer paymentMethod;

    /**
     * 计算时间
     */
    private Date comparisonTime;

    /**
     * 计算时间
     */
    private Date calculateTime;

    /**
     * 兑付时间
     */
    private Date cashTime;

    /**
     * 兑付状态1计算状态2已经兑付
     */
    private Integer cashStatus;

    /**
     * 是否已经满足条件状态1未满足2已经满足
     */
    private Integer conditionStatus;

    private List<AgreementRebateOrderDetailRequest> agreementRebateOrderDetailList;

    @Data
    public static class AgreementRebateOrderDetailRequest extends BaseRequest {
        /**
         * 销售主体Eid
         */
        private Long eid;

        /**
         * 采购方eid
         */
        private Long secondEid;

        /**
         * 协议Id
         */
        private Long agreementId;

        /**
         * 协议条件Id
         */
        private Long agreementConditionId;

        /**
         * 版本号
         */
        private Integer version;

        /**
         * 账号信息
         */
        private String easAccount;

        /**
         * 订单Id
         */
        private Long orderId;

        /**
         * 退货单Id
         */
        private Long returnId;

        private Long settlementId;

        private Date deliveryTime;

        private Integer type;

        /**
         * 订单明细Id
         */
        private Long orderDetailId;

        /**
         * 以岭商品Id
         */
        private Long goodsId;

        /**
         * 商品购买总额
         */
        private BigDecimal goodsAmount;

        /**
         * 购买数量
         */
        private Long goodsQuantity;

        /**
         * 协议返还金额
         */
        private BigDecimal discountAmount;

        /**
         * 协议政策
         */
        private Integer policyValue;

        /**
         * 计算时间
         */
        private Date comparisonTime;
    }
}
