package com.yiling.admin.data.center.standard.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.annotations.ExcelShow;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

/**
 * @author:wei.wang
 * @date:2021/5/25
 */
@Data
public class StandardHealthImportExcelForm implements IExcelModel, IExcelDataModel {


    /**
     * 是否国产：  1 国产 2进口 3 出口
     */
    @NotNull(message = "不能为空")
    @Excel(name = "*商品类型", replace = {"国产_1", "进口_2", "出口_3"})
    private Integer isCn;

    @ExcelShow
    @NotBlank(message = "不能为空")
    @Excel(name = "*产品名")
    private String name;

    @ExcelShow
    @NotBlank(message = "不能为空")
    @Excel(name = "*批准文号")
    private String  licenseNo;

    @ExcelShow
    @NotBlank(message = "不能为空")
    @Excel(name = "*生产厂家")
    private String manufacturer;

    @NotBlank(message = "不能为空")
    @Excel(name = "*一级分类")
    private String standardCategoryName1;

    @NotBlank(message = "不能为空")
    @Excel(name = "*二级分类")
    private String standardCategoryName2;


    @Excel(name = "生产地址")
    private String manufacturerAddress;

    @Excel(name = "批准日期")
    private String approvalDate;

    @Excel(name = "执行标准")
    private String executiveStandard;

    @Excel(name = "原料")
    private String rawMaterial;

    @Excel(name = "辅料")
    private String ingredients;

    @Excel(name = "适宜人群")
    private String suitablePeople;

    @Excel(name = "不适宜人群")
    private String unsuitablePeople;

    @Excel(name = "保健功能")
    private String healthcareFunction;

    @Excel(name = "食用量及食用方法")
    private String usageDosage;

    @Excel(name = "保质期")
    private String expirationDate;

    @Excel(name = "贮藏方法")
    private String store;


    @NotBlank(message = "不能为空")
    @Excel(name = "*规格")
    private String sellSpecifications;

    @NotBlank(message = "不能为空")
    @Excel(name = "*单位")
    private String unit;

    @Excel(name = "条形码")
    private String barcode;

    @Excel(name = "YPID")
    private String ypidCode;

    @Excel(name = "错误信息")
    private String errorMsg;

    private Integer rowNum;


}
