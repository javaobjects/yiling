package com.yiling.admin.data.center.goods.form;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.annotations.ExcelRepet;
import com.yiling.framework.common.annotations.ExcelShow;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

/**
 * 导入招标挂网价 Form
 *
 * @author: dexi.yao
 * @date: 2021/07/08
 */
@Data
public class ImportGoodsBiddingPriceForm implements IExcelModel, IExcelDataModel {


	@ExcelRepet
    @ExcelShow
    @Excel(name = "商品ID")
	@NotNull
    private Long  goodsId;

    @NotEmpty
	@Length(max = 50)
    @ExcelShow
    @Excel(name = "商品名称")
    private String goodsName;

    @NotEmpty
	@Length(max = 50)
    @ExcelShow
    @Excel(name = "批准文号")
    private String licenseNo;

    @NotEmpty
	@Length(max = 50)
    @ExcelShow
    @Excel(name = "生产厂家")
    private String manufacturer;

//    @NotNull
//    @ExcelShow
////	@Range(min = 0, message = "基价不正确")
//    @Excel(name = "基价")
//    private BigDecimal price;

    @ExcelShow
    @Excel(name = "北京市")
    private BigDecimal  location_110000;

    @ExcelShow
    @Excel(name = "天津市")
    private BigDecimal  location_120000;

    @ExcelShow
    @Excel(name = "河北省")
    private BigDecimal  location_130000;

    @ExcelShow
    @Excel(name = "山西省")
    private BigDecimal  location_140000;

    @ExcelShow
    @Excel(name = "内蒙古自治区")
    private BigDecimal  location_150000;

    @ExcelShow
    @Excel(name = "辽宁省")
    private BigDecimal  location_210000;

    @ExcelShow
    @Excel(name = "吉林省")
    private BigDecimal  location_220000;

    @ExcelShow
    @Excel(name = "黑龙江省")
    private BigDecimal  location_230000;

    @ExcelShow
    @Excel(name = "上海市")
    private BigDecimal  location_310000;

    @ExcelShow
    @Excel(name = "江苏省")
    private BigDecimal  location_320000;

    @ExcelShow
    @Excel(name = "浙江省")
    private BigDecimal  location_330000;

    @ExcelShow
    @Excel(name = "安徽省")
    private BigDecimal  location_340000;

    @ExcelShow
    @Excel(name = "福建省")
    private BigDecimal  location_350000;

    @ExcelShow
    @Excel(name = "江西省")
    private BigDecimal  location_360000;

    @ExcelShow
    @Excel(name = "山东省")
    private BigDecimal  location_370000;

    @ExcelShow
    @Excel(name = "河南省")
    private BigDecimal  location_410000;

    @ExcelShow
    @Excel(name = "湖北省")
    private BigDecimal  location_420000;

    @ExcelShow
    @Excel(name = "湖南省")
    private BigDecimal  location_430000;

    @ExcelShow
    @Excel(name = "广东省")
    private BigDecimal  location_440000;

    @ExcelShow
    @Excel(name = "广西壮族自治区")
    private BigDecimal  location_450000;

    @ExcelShow
    @Excel(name = "海南省")
    private BigDecimal  location_460000;

    @ExcelShow
    @Excel(name = "重庆市")
    private BigDecimal  location_500000;

    @ExcelShow
    @Excel(name = "四川省")
    private BigDecimal  location_510000;

    @ExcelShow
    @Excel(name = "贵州省")
    private BigDecimal  location_520000;

    @ExcelShow
    @Excel(name = "云南省")
    private BigDecimal  location_530000;

    @ExcelShow
    @Excel(name = "西藏自治区")
    private BigDecimal  location_540000;

    @ExcelShow
    @Excel(name = "陕西省")
    private BigDecimal  location_610000;

    @ExcelShow
    @Excel(name = "甘肃省")
    private BigDecimal  location_620000;

    @ExcelShow
    @Excel(name = "青海省")
    private BigDecimal  location_630000;

    @ExcelShow
    @Excel(name = "宁夏回族自治区")
    private BigDecimal  location_640000;

    @ExcelShow
    @Excel(name = "新疆维吾尔自治区")
    private BigDecimal  location_650000;

    @ExcelShow
    @Excel(name = "台湾省")
    private BigDecimal  location_710000;

    @ExcelShow
    @Excel(name = "香港特别行政区")
    private BigDecimal  location_810000;

    @ExcelShow
    @Excel(name = "澳门特别行政区")
    private BigDecimal  location_820000;

    @Excel(name = "错误信息")
    private String  errorMsg;

    private Integer rowNum;
}
