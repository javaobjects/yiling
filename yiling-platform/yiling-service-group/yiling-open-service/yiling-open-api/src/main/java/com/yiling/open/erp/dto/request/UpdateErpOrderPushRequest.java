package com.yiling.open.erp.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * ERP同步后修改订单
 * @author:wei.wang
 * @date:2021/7/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateErpOrderPushRequest extends BaseRequest {
    /**
     * 订单Id
     */
    private Long orderId;

    private Integer pushType;

    /**
     * ERP推送状态：1-未推送 2-推送成功 3-推送失败
     */
    private Integer erpPushStatus;

    /**
     * ERP订单号
     */
    private String erpOrderNo;

    private String erpFlowNo;

    /**
     * ERP推送备注
     */
    private String erpPushRemark;
}
