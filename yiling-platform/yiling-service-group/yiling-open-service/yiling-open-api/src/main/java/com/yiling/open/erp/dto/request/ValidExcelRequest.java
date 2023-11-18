package com.yiling.open.erp.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2021/8/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ValidExcelRequest extends BaseRequest {

	/**
	 * 批复代码
	 */
    private String approvalCode;

	/**
	 * 申请日期
	 */
    private String applicantDate;

	/**
	 * 归属年度
	 */
    private String ascriptionYear;

	/**
	 * 归属月度
	 */
    private String ascriptionMon;

	/**
	 * 省份
	 */
    private String province;

	/**
	 * 返利归属企业
	 */
    private String FirstOrderBusinessName;

	/**
	 * 返利归属企业编码
	 */
    private String FirstOrderBusinessCode;

	/**
	 * 冲红金额
	 */
    private String rushReAmount;

	/**
	 * 返利种类
	 */
    private String rebateVariety;

	/**
	 * 发货组织
	 */
    private String deliveryOrg;

	/**
	 * 费用科目
	 */
    private String CostSubject;

	/**
	 * 费用归属部门
	 */
    private String remarkDept;

	/**
	 * 执行部门
	 */
    private String costOfDept;

	/**
	 * 品种
	 */
    private String variety;

	/**
	 * 确认函编码
	 */
    private String confirmationLetter;

	/**
	 * 上传日期
	 */
    private String uploadDate;

	/**
	 * 预留1
	 */
    private String reservedStr1;

	/**
	 * 预留2
	 */
    private String reservedStr2;

	/**
	 * 预留3
	 */
    private String reserved3;

	/**
	 * 预留4
	 */
    private String reservedStr4;

	/**
	 * 预留5
	 */
    private String reservedStr5;

	/**
	 * 备注
	 */
    private String remark;

}
