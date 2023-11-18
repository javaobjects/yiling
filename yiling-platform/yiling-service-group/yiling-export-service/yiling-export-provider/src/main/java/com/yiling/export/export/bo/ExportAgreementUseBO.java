package com.yiling.export.export.bo;

import lombok.Data;

/**
 * 导入招标挂网价 Form
 *
 * @author: dexi.yao
 * @date: 2021/07/08
 */
@Data
public class ExportAgreementUseBO {


	/**
	 * 申请单号
	 */
	private String applicantCode;

	/**
	 * 申请企业名称
	 */
	private String name;

	/**
	 * 企业名称ID
	 */
	private Long eid;

	/**
	 * 所属省份
	 */
	private String provinceName;

	/**
	 * 使用返利金额
	 */
	private String totalAmount;

	/**
	 * 申请类型
	 */
	private String executeMeans;

	/**
	 * 申请时间
	 */
	private String createTime;


	/**
	 * 审核状态
	 */
	private String status;

	/**
	 * 审核时间
	 */
	private String updateTime;

	/**
	 * 发货组织
	 */
	private String sellerName;

	/**
	 * 申请人
	 */
	private String updateUserName;

}
