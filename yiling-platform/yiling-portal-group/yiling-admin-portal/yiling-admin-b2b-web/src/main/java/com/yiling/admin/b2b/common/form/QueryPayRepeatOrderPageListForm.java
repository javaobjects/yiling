package com.yiling.admin.b2b.common.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryPayRepeatOrderPageListForm extends QueryPageListForm {

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String appOrderNo;

    /**
     * 退款状态
     */
    @ApiModelProperty(value = "退款状态")
    private Integer refundState;

    /**
     * 支付开始时间
     */
    @ApiModelProperty(value = "支付开始时间")
    private Date startPayTime;
    /**
     * 支付结束时间
     */
    @ApiModelProperty(value = "支付结束时间")
    private Date endPayTime;

    /**
     * 支付方式
     */
    @ApiModelProperty(value = "支付方式")
    private String payWay;

    /**
     * 交易类型(1:其他,2:支付,3:在线还款,4:转账,5:会员)
     */
    @ApiModelProperty(value = "单据类型(1:其他,2:支付,3:在线还款,4:转账,5:会员)")
    private Integer tradeType;

    /**
     * 处理状态 1:未处理 2:处理中 3:处理失败,4:已处理
     */
    @ApiModelProperty(value = "处理状态 1:未处理 2:处理中 3:处理失败,4:已处理")
    private Integer dealState;

}
