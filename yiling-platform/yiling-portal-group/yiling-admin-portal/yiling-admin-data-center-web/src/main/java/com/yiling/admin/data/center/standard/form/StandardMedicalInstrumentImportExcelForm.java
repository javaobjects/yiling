package com.yiling.admin.data.center.standard.form;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.annotations.ExcelRepet;
import com.yiling.framework.common.annotations.ExcelShow;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

/**
 * @author shichen
 * @类名 StandardMedicalInstrumentImportExcelForm
 * @描述
 * @创建时间 2022/8/10
 * @修改人 shichen
 * @修改时间 2022/8/10
 **/
@Data
public class StandardMedicalInstrumentImportExcelForm implements IExcelModel, IExcelDataModel {
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

    @NotNull(message = "不能为空")
    @Excel(name = "*商品所属经营范围", replace = {"一类医疗器械_1", "二类医疗器械_2", "三类医疗器械_3"})
    private Integer businessScope;

    @ExcelRepet
    @ExcelShow
    @NotBlank(message = "不能为空")
    @Excel(name = "*备案凭证编号/注册证编号")
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

    @Excel(name = "商品名")
    private String aliasName;

    @Excel(name = "生产地址")
    private String manufacturerAddress;

    @Excel(name = "结构组成")
    private String structure;

    @Excel(name = "注意事项")
    private String noteEvents;

    @Excel(name = "适用范围")
    private String useScope;

    @Excel(name = "储藏条件")
    private String storageConditions;

    @Excel(name = "使用说明")
    private String usageDosage;

    @Excel(name = "包装")
    private String packingInstructions;

    @Excel(name = "有效期")
    private String expirationDate;

    @Excel(name = "备注")
    private String remark;

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
