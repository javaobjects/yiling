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
@ApiModel("预售订单下单提交")
public class PresalOrderSubmitForm {

    @NotNull
    @Min(value = 1)
    @ApiModelProperty(value = "收货地址ID", required = true)
    private Long addressId;

    @NotEmpty
    @ApiModelProperty(value = "卖家预售订单列表", required = true)
    private List<DistributorPreOrderForm> distributorPreOrderList;

    /**
     * 配送商订单 Form
     */
    @Data
    public static class DistributorPreOrderForm {

        @NotNull
        @Min(value = 1)
        @ApiModelProperty(value = "卖家企业ID", required = true)
        private Long distributorEid;

        @NotNull
        @Min(value = 1)
        @ApiModelProperty(value = "卖家支付方式", required = true)
        private Integer paymentMethod;

        @Length(max = 200)
        @ApiModelProperty(value = "买家留言")
        private String buyerMessage;

    }
}
