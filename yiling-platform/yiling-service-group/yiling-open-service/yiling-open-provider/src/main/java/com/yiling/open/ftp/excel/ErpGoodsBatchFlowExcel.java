package com.yiling.open.ftp.excel;

import java.math.BigDecimal;

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
public class ErpGoodsBatchFlowExcel implements IExcelModel, IExcelDataModel {

    private static final long serialVersionUID = -4672345329942342009L;

    @Excel(name = "库存主键ID")
    private String gbIdNo;

    @Excel(name = "部门编号")
    protected String suDeptNo;

    @Excel(name = "入库时间",importFormat = "yyyy-MM-dd")
    protected String gbTime;

    @Excel(name = "药品内码")
    @NotEmpty(message="商品内码不能为空")
    private String inSn;

    @Excel(name = "批次号")
    private String gbBatchNo;

    @Excel(name = "生产时间",importFormat = "yyyy-MM-dd")
    private String gbProduceTime;

    @Excel(name = "有效期",importFormat = "yyyy-MM-dd")
    private String gbEndTime;

    @Excel(name = "生产地址")
    private String gbProduceAddress;

    @Excel(name = "库存数量")
    private String gbNumber;

    @Excel(name = "商品名称")
    private String gbName;

    @Excel(name = "商品批准文号")
    private String gbLicense;

    @Excel(name = "商品规格")
    private String gbSpecifications;

    @Excel(name = "商品单位")
    private String gbUnit;

    @Excel(name = "生产厂家")
    private String gbManufacturer;


    @Excel(name = "错误信息")
    private String errorMsg;

    private Integer rowNum;
}
