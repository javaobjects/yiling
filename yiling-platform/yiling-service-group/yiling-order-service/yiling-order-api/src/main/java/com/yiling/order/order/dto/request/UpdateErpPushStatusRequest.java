package com.yiling.order.order.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * ERP接受订单返回状态参数
 * @author:wei.wang
 * @date:2021/7/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateErpPushStatusRequest extends BaseRequest {
    /**
     * 订单Id
     */
    private Long orderId;

    /**
     * 发票推送主键
     */
    private String groupNo;

    private String orderSn;
    /**
     * ERP推送状态：1-未推送 2-推送成功 3-推送失败
     */
    private Integer erpPushStatus;

    /**
     * ERP推送备注
     */
    private String erpPushRemark;

}
