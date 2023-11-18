package com.yiling.settlement.report.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-05-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ReportOrderSyncDTO extends BaseDTO {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 卖家eid
     */
    private Long sellerEid;

    /**
     * 买家eid
     */
    private Long buyerEid;

    /**
     * 下单时间
     */
    private Date orderCreateTime;

    /**
     * 收货时间
     */
    private Date receiveTime;

    /**
     * 订单状态：10-待审核 20-待发货 25-部分发货 30-已发货 40-已收货 100-已完成 -10-已取消
     */
    private Integer orderStatus;

    /**
     * 订单来源：1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台
     */
    private Integer orderSource;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款 4-在线支付
     */
    private Integer paymentMethod;

    /**
     * 支付状态：1-待支付 2-已支付
     */
    private Integer paymentStatus;

    /**
     * 报表ID
     */
    private Long reportId;

    /**
     * 报表计算状态：1-未计算 2-已计算
     */
    private Integer reportSettStatus;

    /**
     * 报表状态：0-待返利 1-待运营确认 2-待财务确认 3-财务已确认 4-运营驳回 5-财务驳回 6-管理员驳回
     */
    private Integer reportStatus;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
}
