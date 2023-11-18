package com.yiling.order.order.dto.request;

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
public class UpdateErpOrderDeliveryRequest extends BaseRequest {
    /**
     * 订单Id
     */
    private Long id;

    /**
     * ERP推送出库状态：1-未推送 2-推送成功 3-推送失败 4-eas提取成功 5-eas提取失败
     */
    private Integer erpDeliveryStatus;


    /**
     * ERP推送出库备注
     */
    private String erpDeliveryRemark;
}
