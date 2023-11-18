package com.yiling.data.center.admin.enterprisesupplier.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 更新供应商 Form
 * </p>
 *
 * @author lun.yu
 * @date 2023-06-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateSupplierForm extends BaseForm {

    /**
     * ID
     */
    @NotNull
    @ApiModelProperty(value = "ID", required = true)
    private Long id;

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
