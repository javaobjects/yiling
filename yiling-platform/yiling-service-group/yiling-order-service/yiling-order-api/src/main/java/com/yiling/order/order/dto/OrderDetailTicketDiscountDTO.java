package com.yiling.order.order.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单明细票折信息 是根据明细id和票折单号维度
 * @author:wei.wang
 * @date:2021/7/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderDetailTicketDiscountDTO extends BaseDTO {
    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 申请Id
     */
    private Long applyId;

    /**
     * ERP出库单号
     */
    private String erpDeliveryNo;

    /**
     * 订单明细ID
     */
    private Long detailId;

    /**
     * 票折单号
     */
    private String ticketDiscountNo;

    /**
     * 使用票折金额
     */
    private BigDecimal useAmount;

    /**
     * 退还票折金额
     */
    private BigDecimal returnAmount;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

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
