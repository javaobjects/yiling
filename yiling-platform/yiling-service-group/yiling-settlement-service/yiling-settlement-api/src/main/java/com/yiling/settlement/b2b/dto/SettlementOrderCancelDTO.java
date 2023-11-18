package com.yiling.settlement.b2b.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-10-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SettlementOrderCancelDTO extends BaseDTO {

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 卖家企业id
     */
    private Long sellerEid;

    /**
     * 买家企业id
     */
    private Long buyerEid;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款 4-在线支付
     */
    private Integer paymentMethod;

    /**
     * 预付金额
     */
    private BigDecimal presaleAmount;

    /**
     * 取消时间
     */
    private Date cancelTime;

    /**
     * 订单状态：10-待审核 20-待发货 25-部分发货 30-已发货 40-已收货 100-已完成 -10-已取消
     */
    private Integer orderStatus;

    /**
     * 下单时间
     */
    private Date orderCreatTime;

    /**
     * 结算单生成状态：1-未生成 2-已生成
     */
    private Integer generateStatus;

    /**
     * 是否可生成结算单：1-不可生成 2-可生成
     */
    private Integer dataStatus;

    /**
     * 当前订单数据同步状态：1-成功 2-失败
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 备注
     */
    private String remark;
}
