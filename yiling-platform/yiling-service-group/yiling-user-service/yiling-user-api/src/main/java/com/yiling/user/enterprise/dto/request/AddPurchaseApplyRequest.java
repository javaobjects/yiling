package com.yiling.user.enterprise.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 添加采购关系申请 Request
 *
 * @author: lun.yu
 * @date: 2022/01/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddPurchaseApplyRequest extends BaseRequest {

    /**
     * 采购商企业ID
     */
    @NotNull
    private Long customerEid;

    /**
     * 企业ID
     */
    @NotNull
    private Long eid;
}
