package com.yiling.sales.assistant.app.order.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.sales.assistant.app.order.vo
 * @date: 2021/12/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CustomerSendOrderVO extends BaseVO {

    @ApiModelProperty(value = "货款总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "支付总金额")
    private BigDecimal totalPayAmount;

    /**
     * 确认订单列表
     */
    @ApiModelProperty(value = "确认订单列表")
    private List<SendOrderVO> sendOrderVoList;

    @Data
    public static class SendOrderVO  extends BaseVO{

        /**
         * 订单号
         */
        @ApiModelProperty(value = "订单号")
        private String orderNo;

        /**
         * "配送商名称/卖家名称
         */
        @ApiModelProperty(value = "配送商名称/卖家名称")
        private String sellerEname;

        /**
         * 商品总金额
         */
        @ApiModelProperty(value = "货款总金额")
        private BigDecimal amount;

        /**
         * 支付总金额
         */
        @ApiModelProperty(value = "支付总金额")
        private BigDecimal paymentAmount;

    }
}
