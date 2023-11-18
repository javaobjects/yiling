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
public class ExportAgreementApplyDetailBO {


	/**
	 * 申请单号
	 */
	private String code;

	/**
	 * 入账企业ID
	 */
	private Long entryEid;

	/**
	 * 入账企业名称
	 */
	private String entryName;

	/**
	 * 所属省份
	 */
	private String provinceName;

	/**
	 * 申请明细id
	 */
	private Long detailId;

	/**
	 * 入账类型
	 */
	private String detailType;

	/**
	 * 入账原因
	 */
	private String entryDescribe;


	/**
	 * 归属年度
	 */
	private Integer year;

	/**
	 * 归属月度
	 */
	private String month;

	/**
	 * 品种
	 */
	private String goodsName;

	/**
	 * 返利金额
	 */
	private BigDecimal amount;

	/**
	 * 订单数量
	 */
	private Integer orderCount;

	/**
	 * 返利种类
	 */
	private String rebateCategory;

	/**
	 * 销售组织名称
	 */
	private String sellerName;

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

	/**
	 * 申请时间
	 */
	private String applyTime;
}
