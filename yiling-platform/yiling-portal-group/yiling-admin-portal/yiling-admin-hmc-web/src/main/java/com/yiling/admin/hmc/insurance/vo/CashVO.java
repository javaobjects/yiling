package com.yiling.admin.hmc.insurance.vo;

import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 保单兑付记录
 * @author: gxl
 * @date: 2022/4/15
 */
@Data
@Accessors(chain = true)
public class CashVO {
    @ApiModelProperty(value = "id点击查看用")
    private Long id;

    /**
     * ID
     */
    @ApiModelProperty(value = "关联处方")
    private Long orderPrescriptionId;
    /**
     * 平台订单编号
     */
    @ApiModelProperty(value = "平台订单编号")
    private String orderNo;
    /**
     * 第三方兑保编号
     */
    @ApiModelProperty(value = "第三方单号")
    private String thirdConfirmNo;

    /**
     * 订单状态:1-预订单待支付/2-已取消/3-待自提/4-待发货/5-待收货/6-已收货/7-已完成/8-取消已退
     */
    @ApiModelProperty(value = "订单状态:1-预订单待支付/2-已取消/3-待自提/4-待发货/5-待收货/6-已收货/7-已完成/8-取消已退")
    private Integer orderStatus;
    /**
     * 下单时间即兑付申请时间
     */
    @ApiModelProperty(value = "兑付申请时间")
    private Date orderTime;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 关联商品信息
     */
    @ApiModelProperty(value = "关联商品信息")
    List<CashGoodsVO> goodsList;
}