package com.yiling.user.enterprise.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.user.common.util.bean.Eq;
import com.yiling.user.common.util.bean.Like;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 供应商分页列表查询 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-06-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuerySupplierPageRequest extends QueryPageListRequest {

    /**
     * 采购商ID
     */
    @Eq
    private Long customerEid;

    /**
     * 供应商ID
     */
    @Eq
    private Long eid;

    /**
     * 社会统一信用代码
     */
    private String licenseNumber;

    /**
     * ERP供应商名称
     */
    @Like
    private String erpName;

    /**
     * 采购员名称
     */
    @Like
    private String buyerName;

}
