package com.yiling.export.imports.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.export.excel.model.BaseImportModel;
import com.yiling.framework.common.annotations.ExcelShow;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @author shichen
 * @类名 ImportFlowEnterpriseGoodsMappingModel
 * @描述
 * @创建时间 2023/2/28
 * @修改人 shichen
 * @修改时间 2023/2/28
 **/
@Data
public class ImportFlowEnterpriseGoodsMappingModel extends BaseImportModel {
    /**
     * 流向原始名称
     */
    @NotEmpty(message = "不能为空")
    @ExcelShow
    @Excel(name = "*原始商品名称", orderNum = "0")
    private String flowGoodsName;

    /**
     * 流向原始规格
     */
    @NotEmpty(message = "不能为空")
    @ExcelShow
    @Excel(name = "*原始商品规格", orderNum = "1")
    private String flowSpecification;

    /**
     * 流向原始商品内码
     */
    @ExcelShow
    @Excel(name = "原始商品内码", orderNum = "2")
    private String flowGoodsInSn;

    /**
     * 流向原始商品厂家
     */
    @ExcelShow
    @Excel(name = "原始商品厂家", orderNum = "3")
    private String flowManufacturer;

    /**
     * 流向原始商品单位
     */
    @ExcelShow
    @Excel(name = "原始商品单位", orderNum = "4")
    private String flowUnit;

    /**
     * 转换单位：1-乘 2-除
     */
    @NotNull(message = "不能为空")
    @ExcelShow
    @Excel(name = "*转换单位",replace = {"乘_1", "除_2"},orderNum = "5")
    private Integer convertUnit;

    /**
     * 转换系数
     */
    @ExcelShow
    @Excel(name = "*转换系数", orderNum = "6")
    private BigDecimal convertNumber;

    /**
     * crm商品编码
     */
    @ExcelShow
    @Excel(name = "*标准产品编码", orderNum = "7")
    private Long crmGoodsCode;

    /**
     * 标准商品名称
     */
    @NotEmpty(message = "不能为空")
    @ExcelShow
    @Excel(name = "*标准产品名称", orderNum = "8")
    private String goodsName;

    /**
     * 标准商品规格
     */
    private String goodsSpecification;

    /**
     * 经销商编码
     */
    @ExcelShow
    @Excel(name = "*经销商编码", orderNum = "9")
    private Long crmEnterpriseId;

    /**
     * 经销商名称
     */
    @NotEmpty(message = "不能为空")
    @ExcelShow
    @Excel(name = "*经销商名称", orderNum = "10")
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
