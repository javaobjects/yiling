package com.yiling.admin.data.center.report.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单返利报表
 *
 * @author:wei.wang
 * @date:2021/6/18
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderRebateReportVO extends BaseVO {

    /**
     * 订单ID
     */
    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNo;


    /**
     * 买家企业ID
     */
    @ApiModelProperty(value = "采购商ID")
    private Long buyerEid;

    /**
     * 买家名称
     */
    @ApiModelProperty(value = "采购商")
    private String buyerEname;

    /**
     * 卖家企业ID
     */
    @ApiModelProperty(value = "供应商id")
    private Long sellerEid;

    /**
     * 卖家名称
     */
    @ApiModelProperty(value = "供应商")
    private String sellerEname;

    /**
     * 支付状态
     */
    @ApiModelProperty(value = "支付状态：1-待支付 2-已支付")
    private Integer paymentStatus;

    /**
     * 支付类型：1-线下支付 2-在线支付
     */
    @ApiModelProperty(value = "支付类型：1-线下支付 2-在线支付")
    private Integer paymentType;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款
     */
    @ApiModelProperty(value = "支付方式：1-线下支付 2-账期 3-预付款")
    private Integer paymentMethod;

    /**
     * 在线支付渠道
     */
    @ApiModelProperty(value = "在线支付渠道")
    private String payChannel;

    /**
     * 在线支付方式
     */
    @ApiModelProperty(value = "在线支付方式")
    private String paySource;



    /**
     * 收货时间
     */
    @ApiModelProperty(value = "签收时间")
    private Date receiveTime;


    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private Date orderCreateTime;

    /**
     * 订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消
     */
    @ApiModelProperty(value = "订单状态 40-已收货 100-已完成")
    private Integer orderStatus;

    /**
     * 采购商所在省
     */
    @ApiModelProperty(value = "采购商所在省")
    private String provinceName;

    /**
     * 采购商所在市
     */
    @ApiModelProperty(value = "采购商所在市")
    private String cityName;

    /**
     * 采购商所在区
     */
    @ApiModelProperty(value = "采购商所在区")
    private String regionName;

    /**
     * 报表状态：0-待返利 1-待运营确认 2-待财务确认 3-财务已确认 4-运营驳回 5-财务驳回 6-管理员驳回
     */
    @ApiModelProperty(value = "返利状态：0-待返利 1-待运营确认 2-待财务确认 3-财务已确认 4-运营驳回 5-财务驳回 6-管理员驳回")
    private Integer reportStatus;

}
