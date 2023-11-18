package com.yiling.f2b.admin.agreementv2.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.annotations.ExcelRepet;
import com.yiling.framework.common.annotations.ExcelShow;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

/**
 * 导入协议商品 Form
 *
 * @author: lun.yu
 * @date: 2022-03-16
 */
@Data
public class ImportAgreementGoodsForm implements IExcelModel, IExcelDataModel {

    @ExcelRepet
    @ExcelShow
    @Excel(name = "商品ID", orderNum = "0")
    @NotNull(message = "不能为空")
    private Long goodsId;

    @ExcelRepet
    @Excel(name = "规格商品ID", orderNum = "1")
    @NotNull(message = "不能为空")
    private Long specificationGoodsId;


    @Excel(name = "商品名称", orderNum = "2")
    @NotEmpty(message = "不能为空")
    private String goodsName;

    @Excel(name = "规格", orderNum = "3")
    private String specifications;

    @Excel(name = "生产厂家名称", orderNum = "4")
    private String manufacturerName;

    @Excel(name = "错误信息", orderNum = "5")
    private String errorMsg;

    private Integer rowNum;
}
