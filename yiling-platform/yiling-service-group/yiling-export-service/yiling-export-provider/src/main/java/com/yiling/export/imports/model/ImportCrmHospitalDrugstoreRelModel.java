package com.yiling.export.imports.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.export.excel.model.BaseImportModel;
import com.yiling.framework.common.annotations.ExcelShow;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import lombok.Data;

/**
 * @author fucheng.bai
 * @date 2023/6/1
 */
@Data
public class ImportCrmHospitalDrugstoreRelModel extends BaseImportModel {

    /**
     * 院外药店机构编码
     */
    @NotNull(message = "不可为空")
    @ExcelShow
    @Excel(name = "院外药店机构编码", orderNum = "0")
    private Long drugstoreOrgId;

    /**
     * 院外药店机构名称
     */
    @NotEmpty(message = "不可为空")
    @Length(max = 100)
    @ExcelShow
    @Excel(name = "院外药店名称", orderNum = "1")
    private String drugstoreOrgName;

    /**
     * 医疗机构编码
     */
    @NotNull(message = "不可为空")
    @ExcelShow
    @Excel(name = "医疗机构编码", orderNum = "2")
    private Long hospitalOrgId;

    /**
     * 医疗机构名称
     */
    @NotEmpty(message = "不可为空")
    @Length(max = 100)
    @ExcelShow
    @Excel(name = "医疗机构名称", orderNum = "3")
    private String hospitalOrgName;

    @ExcelIgnore
    private Long categoryId;

    /**
     * 品种名称
     */
    @NotEmpty(message = "不可为空")
    @Length(max = 100)
    @ExcelShow
    @Excel(name = "品种名称", orderNum = "4")
    private String categoryName;

    @ExcelIgnore
    private Long crmGoodsCode;

    /**
     * 标准产品名称
     */
    @NotEmpty(message = "不可为空")
    @Length(max = 100)
    @ExcelShow
    @Excel(name = "产品名称", orderNum = "5")
    private String crmGoodsName;

    @NotEmpty(message = "不可为空")
    @ExcelShow
    @Excel(name = "产品规格", orderNum = "6")
    private String crmGoodsSpec;

    /**
     * 开始生效时间
     */
    @NotEmpty(message = "不可为空")
    @ExcelShow
    @Excel(name = "开始生效日期", orderNum = "7")
    private String effectStartTime;

    /**
     * 结束生效时间
     */
    @NotEmpty(message = "不可为空")
    @ExcelShow
    @Excel(name = "结束生效日期", orderNum = "8")
    private String effectEndTime;

}
