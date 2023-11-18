package com.yiling.data.center.admin.enterprisesupplier.form;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 供应商分页列表查询 Form
 * </p>
 *
 * @author lun.yu
 * @date 2023-06-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuerySupplierPageForm extends QueryPageListForm {

    /**
     * 供应商ID
     */
    @ApiModelProperty("供应商ID")
    private Long eid;

    /**
     * 社会统一信用代码
     */
    @ApiModelProperty("社会统一信用代码")
    private String licenseNumber;

    /**
     * ERP供应商名称
     */
    @Length(max = 50)
    @ApiModelProperty("ERP供应商名称")
    private String erpName;

    /**
     * 采购员名称
     */
    @Length(max = 50)
    @ApiModelProperty("采购员名称")
    private String buyerName;

}
