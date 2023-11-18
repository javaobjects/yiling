package com.yiling.hmc.order.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.yiling.hmc.order.dto.OrderDetailDTO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 保单兑付记录
 * @author: gxl
 * @date: 2022/4/15
 */
@Data
@Accessors(chain = true)
public class OrderBO implements Serializable {

    private static final long serialVersionUID = -217101627488116759L;

    /**
     * ID
     */
    private Long id;

    private Long orderPrescriptionId;

    /**
     * 平台订单编号
     */
    private String orderNo;
    /**
     * 第三方兑保编号
     */
    private String thirdConfirmNo;

    /**
     * 订单状态:1-预订单待支付/2-已取消/3-待自提/4-待发货/5-待收货/6-已收货/7-已完成/8-取消已退
     */
    private Integer orderStatus;
    /**
     * 下单时间即兑付申请时间
     */
    private Date orderTime;
    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 关联商品信息
     */
    List<OrderDetailDTO> goodsList;
}