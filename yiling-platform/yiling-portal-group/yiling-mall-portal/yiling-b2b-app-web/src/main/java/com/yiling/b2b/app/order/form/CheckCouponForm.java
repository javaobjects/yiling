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
public class CheckCouponForm {

    @NotEmpty
    @ApiModelProperty(value = "卖家订单列表", required = true)
    private List<DistributorForm> distributorOrderList;

    @ApiModelProperty(value = "平台优惠劵ID")
    private Long customerPlatformCouponId;
    /**
     * 配送商订单 Form
     */
    @Data
    @ApiModel("优惠劵配送商")
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

        @Length(max = 200)
        @ApiModelProperty(value = "买家留言")
        private String buyerMessage;

    }

}
