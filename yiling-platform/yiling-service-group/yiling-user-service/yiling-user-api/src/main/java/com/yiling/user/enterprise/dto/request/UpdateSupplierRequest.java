package com.yiling.user.enterprise.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 更新供应商 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-06-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateSupplierRequest extends BaseRequest {

    /**
     * ID
     */
    private Long id;

    /**
     * ERP供应商名称
     */
    private String erpName;

    /**
     * ERP编码
     */
    private String erpCode;

    /**
     * ERP内码
     */
    private String erpInnerCode;

    /**
     * 采购员编码
     */
    private String buyerCode;

    /**
     * 采购员名称
     */
    private String buyerName;

}
