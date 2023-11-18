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
public class StandardMaterialsImportExcelForm implements IExcelModel, IExcelDataModel {

    /**
     * 是否国产：  1 国产 2进口 3 出口
     */
    @NotNull(message = "不能为空")
    @Excel(name = "*商品类型", replace = {"国产_1", "进口_2", "出口_3"})
    private Integer isCn;

    @ExcelShow
    @NotBlank(message = "不能为空")
    @Excel(name = "*产品名")
    private String  name;

    @ExcelShow
    @NotBlank(message = "不能为空")
    @Excel(name = "*生产地址")
    private String manufacturerAddress;

    @NotBlank(message = "不能为空")
    @Excel(name = "*一级分类")
    private String standardCategoryName1;

    @NotBlank(message = "不能为空")
    @Excel(name = "*二级分类")
    private String standardCategoryName2;

    @Excel(name = "商品名")
    private String aliasName;

    @Excel(name = "来源")
    private String goodsSource;

    @Excel(name = "性状")
    private String drugProperties;

    @Excel(name = "性味")
    private String propertyFlavor;

    @Excel(name = "功效")
    private String effect;

    @Excel(name = "用法用量")
    private String usageDosage;

    @Excel(name = "储藏")
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
