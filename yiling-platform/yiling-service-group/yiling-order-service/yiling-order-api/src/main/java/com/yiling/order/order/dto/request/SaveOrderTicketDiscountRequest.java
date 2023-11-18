package com.yiling.order.order.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 提交发票保存订单票折记录
 * @author:wei.wang
 * @date:2021/7/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrderTicketDiscountRequest extends BaseRequest {
    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 申请id
     */
    private Long applyId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 订单金额
     */
    private BigDecimal totalAmount;

    /**
     * 现折金额
     */
    private BigDecimal cashDiscountAmount;

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



}
