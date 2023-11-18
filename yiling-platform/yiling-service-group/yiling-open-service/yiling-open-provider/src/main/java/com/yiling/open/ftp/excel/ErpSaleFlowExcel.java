package com.yiling.open.ftp.excel;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotEmpty;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2022/2/28
 */
@Data
public class  ErpSaleFlowExcel implements IExcelModel, IExcelDataModel {

    private static final long serialVersionUID = -4672345329942342009L;

    @Excel(name = "销售单号")
    private String soNo;

    @Excel(name = "部门编号")
    protected String suDeptNo;

    @Excel(name = "销售日期",importFormat = "yyyy-MM-dd")
    @NotEmpty(message="销售日期不能为空")
    private String soTime;

    @Excel(name = "客户内码")
    @NotEmpty(message="客户内码不能为空")
    private String enterpriseInnerCode;

    @Excel(name = "客户名称")
    private String enterpriseName;

    @Excel(name = "批次号")
    private String soBatchNo;

    @Excel(name = "销售数量")
    private String soQuantity;

    @Excel(name = "生产时间",importFormat = "yyyy-MM-dd")
    private String soProductTime;

    @Excel(name = "有效期",importFormat = "yyyy-MM-dd")
    private String soEffectiveTime;

    @Excel(name = "价格")
    private String soPrice;

    @Excel(name = "商品内码")
    @NotEmpty(message="商品内码不能为空")
    private String goodsInSn;

    @Excel(name = "商品名称")
    private String goodsName;

    @Excel(name = "商品批准文号")
    private String soLicense;

    @Excel(name = "商品规格")
    private String soSpecifications;

    @Excel(name = "商品单位")
    private String soUnit;

    @Excel(name = "生产厂家")
    private String soManufacturer;

    @Excel(name = "数据来源")
    private String soSource;

    @Excel(name = "下单时间",importFormat = "yyyy-MM-dd",format = "yyyy-MM-dd")
    private String orderTime;

    @Excel(name = "统一信用代码")
    private String licenseNumber;

    @Excel(name = "错误信息")
    private String errorMsg;

    private Integer rowNum;
}
