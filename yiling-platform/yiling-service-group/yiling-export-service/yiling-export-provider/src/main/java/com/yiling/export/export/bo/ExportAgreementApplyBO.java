package com.yiling.export.export.bo;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 协议申请导出BO
 *
 * @author: dexi.yao
 * @date: 2021/07/08
 */
@Data
public class ExportAgreementApplyBO {


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
	 * 返利总金额
	 */
	private BigDecimal totalAmount;

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
	 * 返利所属企业ID
	 */
	private Long eid;

	/**
	 * 返利所属企业名称
	 */
	private String name;

	/**
	 * 操作人
	 */
	private String user;
}
