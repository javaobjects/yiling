package com.yiling.user.enterprise.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 更新企业状态 Request
 *
 * @author: xuan.zhou
 * @date: 2021/5/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateErpStatusRequest extends BaseRequest {

    /**
     * 企业ID
     */
    @NotNull
    private Long id;

    /**
     * ERP对接级别：0-未对接 1-基础对接 2-订单对接 3-发货单对接
     */
    @NotNull
    private Integer erpSyncLevel;
}
