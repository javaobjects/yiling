package com.yiling.export.imports.model;

import javax.validation.constraints.NotEmpty;

import com.yiling.export.excel.model.BaseImportModel;
import com.yiling.framework.common.annotations.ExcelRepet;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 导入锁定用户 Model
 *
 * @author: lun.yu
 * @date: 2022-04-02
 */
@Data
public class ImportLockCustomerModel extends BaseImportModel {

    @Excel(name = "企业名称", orderNum = "0")
    @NotEmpty(message = "不能为空")
    private String name;

    @Excel(name = "企业类型名称", orderNum = "1")
    @NotEmpty(message = "不能为空")
    private String typeName;

    @ExcelRepet
    @Excel(name = "执业许可证号/社会信用统一代码", orderNum = "2")
    @NotEmpty(message = "不能为空")
    private String licenseNumber;

    @Excel(name = "状态", orderNum = "3")
    @NotEmpty(message = "不能为空")
    private String statusName;

    @Excel(name = "所属省份名称", orderNum = "4")
    @NotEmpty(message = "不能为空")
    private String provinceName;

    @Excel(name = "所属城市名称", orderNum = "5")
    @NotEmpty(message = "不能为空")
    private String cityName;

    @Excel(name = "所属区县名称", orderNum = "6")
    @NotEmpty(message = "不能为空")
    private String regionName;

    @Excel(name = "详细地址", orderNum = "7")
    @NotEmpty(message = "不能为空")
    private String address;

    @Excel(name = "所属板块", orderNum = "8")
    @NotEmpty(message = "不能为空")
    private String plate;
    
}
