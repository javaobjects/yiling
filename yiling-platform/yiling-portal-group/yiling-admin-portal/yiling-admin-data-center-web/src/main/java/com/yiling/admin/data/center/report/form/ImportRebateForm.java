package com.yiling.admin.data.center.report.form;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.annotations.ExcelShow;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

/**
 * 导入参数 Form
 *
 * @author: dexi.yao
 * @date: 2022/07/14
 */
@Data
public class ImportRebateForm implements IExcelModel, IExcelDataModel {

    @ExcelShow
    @Excel(name = "*报表id")
    @NotNull(message ="不能为空")
    private Long  reportId;

    @ExcelShow
    @Excel(name = "*报表类型")
    private String  reportType;

    @ExcelShow
    @Excel(name = "*报表明细id")
    @NotNull(message ="不能为空")
    private Long  reportDetailId;

    @ExcelShow
    @Excel(name = "商业名称")
    private String  entName;

    @ExcelShow
    @Excel(name = "商品名称")
    private String  goodsName;

    @ExcelShow
    @Excel(name = "客户编码")
    private String  customerCode;

    @ExcelShow
    @Excel(name = "客户名称")
    private String  customerName;


    @NotNull(message ="不能为空")
    @ExcelShow
	@Range(min = 1, max = 1, message = "返利状态不正确")
    @Excel(name = "*返利状态（1.已返利）")
    private Integer rebateStatus;

    @Excel(name = "错误信息")
    private String  errorMsg;

    private Integer rowNum;
}
