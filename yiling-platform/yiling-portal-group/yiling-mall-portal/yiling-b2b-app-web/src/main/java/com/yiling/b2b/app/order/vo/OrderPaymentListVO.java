package com.yiling.b2b.app.order.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * B2B移动端订单账期列表VO
 * @author:wei.wang
 * @date:2021/10/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderPaymentListVO extends BaseVO {

    /**
     * 订单编码
     */
    @ApiModelProperty(value = "订单编码")
    private String orderNo;

    /**
     *  还款状态 1-待还款 2-已还款
     */
    @ApiModelProperty(value = "还款状态 ：1-待还款 3-已还款 4-商家确认还款(已还款)")
    private Integer repaymentStatus;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String sellerEname;

    /**
     * 供应商id
     */
    @ApiModelProperty(value = "供应商id")
    private Long sellerEid;

    /**
     *采购商名称
     */
    @ApiModelProperty(value = "采购商名称")
    private String buyerEname;

    /**
     * 采购商id
     */
    @ApiModelProperty(value = "采购商id")
    private Long buyerEid;

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private Date createTime;

    /**
     * 支付金额
     */
    @ApiModelProperty(value = "支付金额")
    private BigDecimal payAmount;

    /**
     * 支付金额
     */
    @ApiModelProperty(value = "还款金额")
    private BigDecimal stayPaymentAmount;

    /**
     * 还款时间
     */
    @ApiModelProperty(value = "还款时间")
    private Date repaymentTime;

    /**
     * 商品种类
     */
    @ApiModelProperty(value = "商品种类")
    private Integer goodsType;

    /**
     * 商品件数
     */
    @ApiModelProperty(value = "商品件数")
    private Integer goodsNumber;

    /**
            * 商品信息
     */
    @ApiModelProperty(value = "商品信息")
    private List<OrderGoodsVO> goodsList;

}
