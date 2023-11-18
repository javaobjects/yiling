package com.yiling.f2b.admin.enterprise.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.annotations.ExcelShow;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

/**
 * 导入采购关系 Form
 *
 * @author: yuecheng.chen
 * @date: 2021/6/9
 */
@Data
public class ImportPurchaseRelationForm implements IExcelModel, IExcelDataModel {

    @Excel(name = "采购商ID")
    @NotNull(message = "采购商ID不能为空")
    private Long buyerEid;

    @ExcelShow
    @Excel(name = "采购商名称")
    private String buyerName;

    @ExcelShow
    @Excel(name = "采购商渠道类型")
    private String buyerChannelName;

    @Excel(name = "供应商ID")
    @NotNull(message = "供应商ID不能为空")
    private Long sellerEid;

    @ExcelShow
    @Excel(name = "供应商名称")
    private String sellerName;

    @ExcelShow
    @Excel(name = "供应商渠道类型")
    private String sellerChannelName;

    @Excel(name = "错误信息")
    private String  errorMsg;

    private Integer rowNum;
}
