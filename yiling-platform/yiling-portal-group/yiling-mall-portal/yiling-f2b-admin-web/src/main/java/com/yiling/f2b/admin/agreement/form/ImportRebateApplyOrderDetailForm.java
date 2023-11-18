package com.yiling.f2b.admin.agreement.form;

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
public class ImportRebateApplyOrderDetailForm implements IExcelModel, IExcelDataModel {


    @ExcelShow
    @Excel(name = "归属年度")
	@NotNull
    private String  year;

    @ExcelShow
    @Excel(name = "归属月度")
	@NotNull
    private String  moth;

	/**
	 * 申请时间
	 */
	@ExcelShow
	@Excel(name = "申请日期")
	@NotNull
	private String applyTime;

    @ExcelShow
    @Excel(name = "返利金额")
	@NotNull
    private String  amount;

	@NotEmpty
	@Length(max = 50)
    @ExcelShow
    @Excel(name = "发货组织")
    private String sellerName;

	@NotEmpty
	@Length(max = 50)
    @ExcelShow
    @Excel(name = "申请单号")
    private String code;

	@ExcelRepet
	@ExcelShow
	@Excel(name = "申请明细Id")
	@NotNull
	private Long  detailId;

	@NotEmpty
	@Length(max = 50)
	@ExcelShow
	@Excel(name = "返利种类")
	private String rebateCategory;

	@NotEmpty
	@Length(max = 50)
	@ExcelShow
	@Excel(name = "费用科目")
	private String costSubject;

	@NotEmpty
	@Length(max = 50)
	@ExcelShow
	@Excel(name = "费用归属部门")
	private String costDept;

	@NotEmpty
	@Length(max = 50)
	@ExcelShow
	@Excel(name = "执行部门")
	private String executeDept;

	@NotEmpty
	@Length(max = 50)
	@ExcelShow
	@Excel(name = "批复代码")
	private String replyCode;

    @Excel(name = "错误信息")
    private String  errorMsg;

    private Integer rowNum;
}
