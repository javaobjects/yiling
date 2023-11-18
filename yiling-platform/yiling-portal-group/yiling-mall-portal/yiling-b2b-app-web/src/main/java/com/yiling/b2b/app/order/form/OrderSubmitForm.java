package com.yiling.b2b.app.order.form;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
@ApiModel("订单提交")
public class OrderSubmitForm {

    @NotNull
    @Min(value = 1)
    @ApiModelProperty(value = "收货地址ID", required = true)
    private Long addressId;

    @NotEmpty
    @ApiModelProperty(value = "卖家订单列表", required = true)
    private List<DistributorOrderForm> distributorOrderList;

    @ApiModelProperty(value = "平台优惠劵Id",notes = "字段名字有问题，调整为最新platformCustomerCouponId")
    @Deprecated
    private Long platfromCustomerCouponId;

    @ApiModelProperty(value = "平台优惠劵Id")
    private Long platformCustomerCouponId;

    @ApiModelProperty(value = "平台支付促销活动Id")
    private Long platformPaymentActivityId;

    @ApiModelProperty(value = "平台支付促销活动规则ID")
    private Long platformActivityRuleIdId;

    /**
     * 配送商订单 Form
     */
    @Data
    public static class DistributorOrderForm {

        @NotNull
        @Min(value = 1)
        @ApiModelProperty(value = "卖家企业ID", required = true)
        private Long distributorEid;

        @NotEmpty
        @ApiModelProperty(value = "购物车ID集合", required = true)
        private List<Long> cartIds;

        @NotNull
        @Min(value = 1)
        @ApiModelProperty(value = "卖家支付方式", required = true)
        private Integer paymentMethod;

        @Length(max = 200)
        @ApiModelProperty(value = "买家留言")
        private String buyerMessage;

        @ApiModelProperty(value = "商家优惠劵Id")
        private Long shopCustomerCouponId;

        @ApiModelProperty(value = "商家支付促销活动Id")
        private Long shopPaymentActivityId;

        @ApiModelProperty(value = "商家支付促销活动规则ID")
        private Long shopActivityRuleIdId;
    }
}
