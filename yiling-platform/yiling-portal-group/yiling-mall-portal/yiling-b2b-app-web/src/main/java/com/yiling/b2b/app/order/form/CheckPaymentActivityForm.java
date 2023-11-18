package com.yiling.b2b.app.order.form;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 结算页支付促销活动选择
 */
@Data
public class CheckPaymentActivityForm {

    @NotEmpty
    @ApiModelProperty(value = "卖家订单列表", required = true)
    private List<DistributorForm> distributorOrderList;

    @ApiModelProperty(value = "平台优惠劵ID")
    private Long customerPlatformCouponId;

    @ApiModelProperty(value = "平台支付促销活动ID")
    private Long platformPaymentActivityId;

    @ApiModelProperty(value = "平台支付促销活动规则ID")
    private Long platformActivityRuleIdId;

    /**
     * 配送商订单 Form
     */
    @Data
    @ApiModel("促销支付配送商")
    public static class DistributorForm {

        @NotNull
        @Min(value = 1)
        @ApiModelProperty(value = "卖家企业ID", required = true)
        private Long distributorEid;

        @ApiModelProperty(value = "商家优惠劵ID")
        private Long customerShopCouponId;

        @NotNull
        @Min(value = 1)
        @ApiModelProperty(value = "卖家支付方式", required = true)
        private Integer paymentMethod;

        @ApiModelProperty(value = "商家支付促销活动ID")
        private Long shopPaymentActivityId;

        @ApiModelProperty(value = "商家支付促销活动规则Id")
        private Long shopActivityRuleIdId;

        @Length(max = 200)
        @ApiModelProperty(value = "买家留言")
        private String buyerMessage;

    }

}
