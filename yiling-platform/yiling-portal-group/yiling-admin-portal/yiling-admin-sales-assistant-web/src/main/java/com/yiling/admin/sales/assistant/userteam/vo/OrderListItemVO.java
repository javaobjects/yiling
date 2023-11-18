package com.yiling.admin.sales.assistant.userteam.vo;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 销售助手后台-用户对应订单列表VO
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/30
 */
@Data
@ApiModel
@Accessors(chain = true)
public class OrderListItemVO {

    /**
     * 订单ID
     */
    @ApiModelProperty("订单ID")
    private Long orderId;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String customerName;

    /**
     * 下单时间
     */
    @ApiModelProperty("下单时间")
    private Date createTime;

    /**
     * 订单金额
     */
    @ApiModelProperty("订单金额")
    private BigDecimal orderAmount;

    /**
     * 订单号
     */
    @ApiModelProperty("订单号")
    private String orderNo;

    /**
     * 订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消
     */
    @ApiModelProperty("订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消")
    private Integer orderStatus;




}
