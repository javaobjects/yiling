package com.yiling.export.export.bo;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 导入招标挂网价 Form
 *
 * @author: dexi.yao
 * @date: 2021/07/08
 */
@Data
public class ExportAgreementUseDetailBO {


	/**
	 * 申请单号
	 */
	private String applicantCode;

	/**
	 * 归属年度
	 */
	private String year;

	/**
	 * 归属月度
	 */
	private String month;

	/**
	 * 金额
	 */
	private BigDecimal amount;

	/**
	 * 返利种类
	 */
	private String rebateCategory;

	/**
	 * 费用科目
	 */
	private String costSubject;

	/**
	 * 费用归属部门
	 */
	private String costDept;

	/**
	 * 执行部门
	 */
	private String executeDept;

	/**
	 * 批复代码
	 */
	private String replyCode;
}
