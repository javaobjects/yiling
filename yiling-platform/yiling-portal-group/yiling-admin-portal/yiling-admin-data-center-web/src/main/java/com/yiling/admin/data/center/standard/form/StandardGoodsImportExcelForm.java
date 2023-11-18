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
 * @author:wei.wang
 * @date:2021/5/25
 */
@Data
public class StandardGoodsImportExcelForm implements IExcelModel, IExcelDataModel {

    /**
     * 是否国产：  1 国产 2进口 3 出口
     */
    @NotNull(message = "不能为空")
    @Excel(name = "*商品类型", replace = {"国产_1", "进口_2", "出口_3"})
    private Integer isCn;

    @ExcelShow
    @NotBlank(message = "不能为空")
    @Excel(name = "*通用名")
    private String  name;

    @ExcelRepet
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

    @NotBlank(message = "不能为空")
    @Excel(name = "*剂型")
    private String gdfName;

    @NotBlank(message = "不能为空")
    @Excel(name = "*剂型规格")
    private String gdfSpecifications;

    @NotNull(message = "不能为空")
    @Excel(name = "*处方类型", replace = {"处方药_1", "甲类非处方_2", "乙类非处方_3", "其他_4"})
    private Integer otcType;

    @Excel(name = "商品名")
    private String aliasName;

    @Excel(name = "生产地址")
    private String manufacturerAddress;

    @Excel(name = "是否医保", replace = {"是_1", "否_2", "未采集到信息_3"})
    private Integer isYb;

    @Excel(name = "管制类型", replace = {"非管制_1", "管制_2"})
    private Integer controlType;

    @Excel(name = "药品本位码")
    private String goodsCode;

    @Excel(name = "复方制剂具体成分")
    private String ingredient;

    @Excel(name = "特殊成分", replace = {"不含麻黄碱_1", "含麻黄碱_2"})
    private Integer specialComposition;

    @Excel(name = "质量标准类别", replace = {"中国药典_1", "地方标准_2","其他_3"})
    private Integer qualityType;

    @Excel(name = "成分")
    private String drugDetails;

    @Excel(name = "性状")
    private String drugProperties;

    @Excel(name = "适应症")
    private String indications;

    @Excel(name = "用法用量")
    private String usageDosage;

    @Excel(name = "不良反应")
    private String adverseEvents;

    @Excel(name = "禁忌")
    private String contraindication;

    @Excel(name = "注意事项")
    private String noteEvents;

    @Excel(name = "药物相互作用")
    private String interreaction;

    @Excel(name = "储藏")
    private String storageConditions;

    @Excel(name = "包装")
    private String packingInstructions;

    @Excel(name = "保质期")
    private String shelfLife;

    @Excel(name = "执行标准")
    private String executiveStandard;

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
