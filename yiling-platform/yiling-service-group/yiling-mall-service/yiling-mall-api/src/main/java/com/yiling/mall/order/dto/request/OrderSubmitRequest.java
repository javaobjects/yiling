package com.yiling.mall.order.dto.request;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.order.order.enums.OrderSourceEnum;
import com.yiling.order.order.enums.OrderTypeEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 创建订单 Request
 *
 * @author: xuan.zhou
 * @date: 2021/6/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderSubmitRequest extends BaseRequest {

    /**
     * ip地址信息
     */
    @NotBlank
    private String ipAddress;

    /**
     * 用户代理
     */
    @NotBlank
    private String userAgent;


    /**
     * 收货地址ID
     */
    @NotNull
    @Min(1)
    private Long addressId;

    /**
     * 配送商订单列表
     */
    @NotEmpty
    private List<DistributorOrderDTO> distributorOrderList;

    /**
     * 商务联系人ID
     */
    private Long contacterId;

    /**
     * 部门ID
     */
    private Long departmentId;

    /**
     * 企业账号
     */
    private String easAccount;

    /**
     * 买家企业ID
     */
    @NotNull
    @Min(1)
    private Long buyerEid;

    /**
     * 订单类型枚举
     */
    @NotNull
    private OrderTypeEnum orderTypeEnum;

    /**
     * 订单来源枚举
     */
    @NotNull
    private OrderSourceEnum orderSourceEnum;

    /**
     * 平台优惠劵ID
     */
    private Long platformCustomerCouponId;

    /**
     * 平台支付促销活动Id
     */
    private Long platformPaymentActivityId;

    /**
     * 平台支付促销活动规则ID
     */
    private Long platformActivityRuleIdId;


    /**
     * 配送商订单 Request
     */
    @Data
    public static class DistributorOrderDTO implements java.io.Serializable {

        /**
         * 配送商企业ID
         */
        @NotNull
        @Min(1)
        private Long distributorEid;

        /**
         * cartIds
         */
        @NotEmpty
        private List<Long> cartIds;

        /**
         * 配送商支付方式：1-线下支付 2-在线支付
         */
        @NotNull
        @Min(1)
        private Integer paymentType;

        /**
         * 支付方式ID
         */
        @NotNull
        private Integer paymentMethod;

        /**
         * 购销合同文件KEY列表
         */
        private List<String> contractFileKeyList;

        /**
         * 合同编号
         */
        @Length(max = 50)
        private String contractNumber;

        /**
         * 买家留言
         */
        @Length(max = 200)
        private String buyerMessage;

        /**
         * 商家优惠劵ID
         */
        private Long shopCustomerCouponId;

        /**
         * 是否有赠品
         */
        private Boolean isHasGift = true;

        /**
         * 商家支付优惠活动ID
         */
        private Long shopPaymentActivityId;

        /**
         * 商家支付促销活动规则ID
         */
        private Long shopActivityRuleIdId;
    }
}
