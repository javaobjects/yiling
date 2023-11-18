package com.yiling.export.imports.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.export.excel.model.BaseImportModel;
import com.yiling.framework.common.annotations.ExcelShow;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @author shichen
 * @类名 ImportFlowEnterpriseSupplierMappingModel
 * @描述
 * @创建时间 2023/6/1
 * @修改人 shichen
 * @修改时间 2023/6/1
 **/
@Data
public class ImportFlowEnterpriseSupplierMappingModel extends BaseImportModel {
    /**
     * 原始供应商名称
     */
    @NotEmpty(message = "不能为空")
    @ExcelShow
    @Excel(name = "原始供应商名称", orderNum = "0")
    private String flowCustomerName;

    /**
     * 标准机构编码
     */
    @NotNull(message = "不能为空")
    @ExcelShow
    @Excel(name = "标准机构编码", orderNum = "1")
    private Long crmOrgId;

    /**
     * 标准机构名称
     */
    @NotEmpty(message = "不能为空")
    @ExcelShow
    @Excel(name = "标准机构名称", orderNum = "2")
    private String orgName;

    /**
     * 标准机构社会信用代码
     */
    private String orgLicenseNumber;

    /**
     * 经销商编码
     */
    @NotNull(message = "不能为空")
    @ExcelShow
    @Excel(name = "经销商编码", orderNum = "3")
    private Long crmEnterpriseId;

    /**
     * 经销商名称
     */
    @NotEmpty(message = "不能为空")
    @ExcelShow
    @Excel(name = "经销商名称", orderNum = "4")
    private String enterpriseName;

    /**
     * 省份代码
     */
    private String provinceCode;

    /**
     * 省份
     */
    private String province;
}
