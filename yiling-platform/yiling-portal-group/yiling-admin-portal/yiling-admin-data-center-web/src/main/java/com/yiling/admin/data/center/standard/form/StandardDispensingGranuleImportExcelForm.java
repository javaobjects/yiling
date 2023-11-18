package com.yiling.admin.data.center.standard.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.annotations.ExcelShow;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

/**
 * @author shichen
 * @类名 StandardDispensingGranuleImportExcelForm
 * @描述
 * @创建时间 2023/5/17
 * @修改人 shichen
 * @修改时间 2023/5/17
 **/
@Data
public class StandardDispensingGranuleImportExcelForm implements IExcelModel, IExcelDataModel {
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
    @Excel(name = "*生产许可证号")
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

    @Excel(name = "等级")
    private String goodsGrade;

    @Excel(name = "执行标准")
    private String executiveStandard;

    @Excel(name = "净含量")
    private String netContent;

    @Excel(name = "原产地")
    private String sourceArea;

    @Excel(name = "包装清单")
    private String packingList;

    @Excel(name = "保质期")
    private String expirationDate;

    @Excel(name = "贮藏")
    private String store;

    @Excel(name = "用法用量")
    private String usageDosage;

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
