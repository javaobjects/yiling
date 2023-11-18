package com.yiling.data.center.admin.enterprisesupplier.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 供应商管理 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-06-16
 */
@Data
@Accessors(chain = true)
public class EnterpriseSupplierVO extends BaseVO {

    /**
     * 供应商名称
     */
    @ApiModelProperty("供应商名称")
    private String ename;

    /**
     * 社会统一信用代码
     */
    @ApiModelProperty("社会统一信用代码")
    private String licenseNumber;

    /**
     * ERP供应商名称
     */
    @ApiModelProperty("ERP供应商名称")
    private String erpName;

    /**
     * ERP编码
     */
    @ApiModelProperty("ERP编码")
    private String erpCode;

    /**
     * ERP内码
     */
    @ApiModelProperty("ERP内码")
    private String erpInnerCode;

    /**
     * 采购员编码
     */
    @ApiModelProperty("采购员编码")
    private String buyerCode;

    /**
     * 采购员名称
     */
    @ApiModelProperty("采购员名称")
    private String buyerName;

}
