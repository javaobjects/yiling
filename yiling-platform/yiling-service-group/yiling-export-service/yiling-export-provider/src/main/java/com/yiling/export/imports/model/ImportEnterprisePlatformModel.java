package com.yiling.export.imports.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.yiling.export.excel.model.BaseImportModel;
import com.yiling.framework.common.annotations.ExcelRepet;
import com.yiling.framework.common.annotations.ExcelShow;
import com.yiling.framework.common.util.Constants;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 导入企业开通平台 Form
 *
 * @author: lun.yu
 * @date: 2022-06-29
 */
@Data
public class ImportEnterprisePlatformModel extends BaseImportModel {

    @NotNull
    @Min(1)
    @ExcelShow
    @Excel(name = "*企业ID", orderNum = "0")
    private Long id;

    @Length(max = 100)
    @ExcelShow
    @Excel(name = "企业名称", orderNum = "1")
    private String name;

    @ExcelShow
    @Excel(name = "企业类型", orderNum = "2")
    private String typeName;

    @Length(max = 50)
    @ExcelRepet
    @ExcelShow
    @Excel(name = "社会统一信用代码/医疗机构执业许可证", orderNum = "3")
    private String licenseNumber;

    @Length(max = 10)
    @ExcelShow
    @Excel(name = "企业联系人姓名", orderNum = "4")
    private String contactor;

    @Pattern(regexp = Constants.REGEXP_MOBILE, message = "企业联系人电话格式不正确")
    @ExcelShow
    @Excel(name = "企业联系人电话", orderNum = "5")
    private String contactorPhone;

    @NotEmpty
    @ExcelShow
    @Excel(name = "*开通产品线", orderNum = "6")
    private String products;

    @ExcelShow
    @Excel(name = "渠道类型", orderNum = "7")
    private String channelName;

    @ExcelShow
    @Excel(name = "药+险业务类型", orderNum = "8")
    private String hmcType;

}
