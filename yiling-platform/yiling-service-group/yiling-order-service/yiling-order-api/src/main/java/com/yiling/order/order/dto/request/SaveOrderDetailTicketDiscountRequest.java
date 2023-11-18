package com.yiling.order.order.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保存单个订单明细票折信息
 * @author:wei.wang
 * @date:2021/8/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrderDetailTicketDiscountRequest extends BaseRequest {
    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 申请id
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


}
