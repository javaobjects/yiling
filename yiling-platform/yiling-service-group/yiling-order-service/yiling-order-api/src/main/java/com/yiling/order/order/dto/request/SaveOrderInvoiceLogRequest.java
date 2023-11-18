package com.yiling.order.order.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保存发票状态变更记录参数
 * @author:wei.wang
 * @date:2021/7/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrderInvoiceLogRequest extends BaseRequest {
    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 发票状态：1-待申请 2-已申请 3-已开票 4-申请驳回 5-已作废
     */
    private Integer status;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;
}
