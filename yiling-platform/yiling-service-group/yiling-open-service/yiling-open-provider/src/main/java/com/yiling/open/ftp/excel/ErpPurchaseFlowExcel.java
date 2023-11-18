package com.yiling.open.ftp.excel;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotEmpty;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-09-23
 */
@Data
public class ErpPurchaseFlowExcel implements IExcelModel, IExcelDataModel {

    private static final long serialVersionUID = -4672345329942342009L;

    @Excel(name = "采购单号")
    private String poNo;

    @Excel(name = "部门编号")
    protected String suDeptNo;

    @Excel(name = "采购单时间",importFormat = "yyyy-MM-dd",format = "yyyy-MM-dd")
    @NotEmpty(message="采购单时间不能为空")
    private String poTime;

    @Excel(name = "供应商内码")
    @NotEmpty(message="供应商内码不能为空")
    private String enterpriseInnerCode;

    @Excel(name = "供应商名称")
    private String enterpriseName;

    @Excel(name = "批次号")
    private String poBatchNo;

    @Excel(name = "采购数量")
    private String poQuantity;

    @Excel(name = "生产时间",importFormat = "yyyy-MM-dd")
    private String poProductTime;

    @Excel(name = "有效期时间",importFormat = "yyyy-MM-dd")
    private String poEffectiveTime;

    @Excel(name = "采购价格")
    private String poPrice;

    @Excel(name = "商品内码")
    @NotEmpty(message="商品内码不能为空")
    private String goodsInSn;

    @Excel(name = "商品名称")
    private String goodsName;

    @Excel(name = "商品批准文号")
    private String poLicense;

    @Excel(name = "商品规格")
    private String poSpecifications;

    @Excel(name = "商品单位")
    private String poUnit;

    @Excel(name = "生产厂家")
    private String poManufacturer;

    @Excel(name = "下单时间",importFormat = "yyyy-MM-dd",format = "yyyy-MM-dd")
    private String orderTime;

    @Excel(name = "错误信息")
    private String errorMsg;

    private Integer rowNum;
}
