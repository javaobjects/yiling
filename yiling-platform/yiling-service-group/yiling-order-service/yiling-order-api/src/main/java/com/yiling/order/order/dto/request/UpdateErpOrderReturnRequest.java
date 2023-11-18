package com.yiling.order.order.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 更新退货单ERP同步状态
 * @author: yong.zhang
 * @date: 2021/7/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateErpOrderReturnRequest extends BaseRequest {

    /**
     * ID
     */
    private Long id;

    /**
     * ERP推送状态：1-未推送 2-推送成功 3-推送失败
     */
    private Integer erpPushStatus;

    private String erpSn;

    /**
     * ERP推送备注
     */
    private String erpPushRemark;
}
