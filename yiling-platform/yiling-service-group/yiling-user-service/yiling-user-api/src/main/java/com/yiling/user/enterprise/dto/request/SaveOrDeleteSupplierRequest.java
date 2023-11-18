package com.yiling.user.enterprise.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 新增供应商 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-06-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrDeleteSupplierRequest extends BaseRequest {

    /**
     * 采购商ID
     */
    private Long customerEid;

    /**
     * 供应商ID
     */
    private Long eid;

}
