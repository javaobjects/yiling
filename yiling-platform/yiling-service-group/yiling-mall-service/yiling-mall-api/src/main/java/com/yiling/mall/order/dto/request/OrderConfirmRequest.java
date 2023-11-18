package com.yiling.mall.order.dto.request;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/** B2B订单确认订单
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.mall.order.dto.request
 * @date: 2021/12/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderConfirmRequest  extends BaseRequest {

    /**
     * 取消订单集合
     */
    private List<Long> cancelOrderIds;

    /**
     * 卖家订单列表
     */
    @NotEmpty
    private List<DistributorOrderDTO> distributorOrderList;
    /**
     * 平台优惠劵ID
     */
    private Long platformCustomerCouponId;

    /**
     * 买家Eid
     */
    private Long buyerEid;

    /**
     * 配送商订单 Form
     */
    @Data
    public static class DistributorOrderDTO extends BaseRequest{

        /**
         * 订单ID
         */
        @NotNull
        @Min(value = 1)
        private Long orderId;

        /**
         * 订单号
         */
        @NotNull
        private String orderNo;

        /**
         * 卖家Eid
         */
        @NotNull
        @Min(value = 1)
        private Long sellerEid;


        /**
         * 配送商Eid
         */
        @NotNull
        @Min(value = 1)
        private Long distributorEid;

        /**
         * 商家优惠劵ID
         */
        private Long shopCustomerCouponId;

        /**
         * 卖家支付方式
         */
        @NotNull
        @Min(value = 1)
        private Integer paymentMethod;

        /**
         * 买家留言
         */
        @Length(max = 200)
        private String buyerMessage;

    }
}
