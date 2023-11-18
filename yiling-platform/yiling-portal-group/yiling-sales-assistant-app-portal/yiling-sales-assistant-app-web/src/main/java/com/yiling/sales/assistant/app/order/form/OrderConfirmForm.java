package com.yiling.sales.assistant.app.order.form;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.util.Constants;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/** b2b订单确认form
 * @author zhigang.guo
 * @date: 2022/10/21
 */
@Data
@Accessors(chain = true)
public class OrderConfirmForm {

    @ApiModelProperty(value = "分享订单转发生成的字符串")
    @NotNull
    private String keyStr;

    /**
     * 手机号
     */
/*    @NotEmpty(message = "^请填写手机号")
    @Pattern(regexp = ErpConstants.REGEXP_MOBILE, message = "^手机号格式有误，请重新填写")
    @ApiModelProperty(value = "手机号", required = true)
    private String mobile;*/

    /**
     * 短信验证码
     */
    /*@NotEmpty(message = "^短信验证码不能为空")
    @ApiModelProperty(value = "短信验证码", required = true)
    private String verifyCode;*/

    @NotEmpty
    @ApiModelProperty(value = "卖家订单列表", required = true)
    private List<CheckCouponForm.OrderForm> orderList;

    @ApiModelProperty(value = "平台优惠劵ID")
    private Long platformCustomerCouponId;
    /**
     * 配送商订单 Form
     */
    @Data
    public static class OrderForm {

        @NotNull
        @Min(value = 1)
        @ApiModelProperty(value = "订单ID", required = true)
        private Long orderId;

        @ApiModelProperty(value = "商家优惠劵ID")
        private Long shopCustomerCouponId;

        @NotNull
        @ApiModelProperty(value = "是否勾选", required = true)
        private Boolean selected;

        @NotNull
        @Min(value = 1)
        @ApiModelProperty(value = "卖家支付方式", required = true)
        private Integer paymentMethod;

        @Length(max = 200)
        @ApiModelProperty(value = "买家留言")
        private String buyerMessage;

    }
}
