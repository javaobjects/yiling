package com.yiling.user.agreement.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-08-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UseDetailDTO extends BaseDTO {

	/**
	 * 申请单id
	 */
	private String applicantId;

	/**
	 * 申请单编号
	 */
	private String applicantCode;

	/**
	 * 所属年度
	 */
	private String year;

	/**
	 * 所属月度
	 */
	private String month;

	/**
	 * 返利金额
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

	/**
	 * 创建人
	 */
	private Long createUser;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改人
	 */
	private Long updateUser;

	/**
	 * 修改时间
	 */
	private Date updateTime;

	/**
	 * 备注
	 */
	private String remark;
}
