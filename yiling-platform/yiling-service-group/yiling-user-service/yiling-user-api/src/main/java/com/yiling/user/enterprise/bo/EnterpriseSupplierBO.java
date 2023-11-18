package com.yiling.user.enterprise.bo;

import java.io.Serializable;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 供应商管理 BO
 * </p>
 *
 * @author lun.yu
 * @date 2023-06-15
 */
@Data
@Accessors(chain = true)
public class EnterpriseSupplierBO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 采购商ID
     */
    private Long customerEid;

    /**
     * 供应商ID
     */
    private Long eid;

    /**
     * 供应商名称
     */
    private String ename;

    /**
     * 社会统一信用代码
     */
    private String licenseNumber;

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
