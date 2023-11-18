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
public class UseDTO extends BaseDTO {

	/**
	 * 申请单id
	 */
	private String applicantId;

	/**
	 * 申请单编号
	 */
	private String applicantCode;

	/**
	 * 申请企业id
	 */
	private Long eid;

	/**
	 * 申请企业easCode
	 */
	private String easCode;

	/**
	 * 申请企业名称
	 */
	private String name;

	/**
	 * 销售组织id
	 */
	private Long sellerEid;

	/**
	 * 销售组织easCode
	 */
	private String sellerCode;

	/**
	 * 销售组织名称
	 */
	private String sellerName;

	/**
	 * 省份code
	 */
	private String provinceCode;

	/**
	 * 省份
	 */
	private String provinceName;

	/**
	 * 申请总金额
	 */
	private BigDecimal totalAmount;

	/**
	 * 执行方式1- 票折 2- 现金 3-冲红 4-健康城卡
	 */
	private Integer executeMeans;

	/**
	 * 申请单状态1-草稿 2-提交 3-审核成功 4-驳回 -5撤回
	 */
	private Integer status;

	/**
	 * 审核意见
	 */
	private String auditRemark;

	/**
	 * 撤回原因
	 */
	private String withdrawRemark;

	/**
	 * 返利种类
	 */
	private String rebateCategory;

	/**
	 * 执行部门
	 */
	private String executeDept;

	/**
	 * 申请时间
	 */
	private Date applicantTime;

	/**
	 * 审核时间
	 */
	private Date auditTime;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 申请人
	 */
	private Long createUser;

	/**
	 * 申请人名称
	 */
	private String createUserName;

	/**
	 * 申请人工号
	 */
	private String createUserCode;

	/**
	 * 修改时间
	 */
	private Date updateTime;

	/**
	 * 修改人
	 */
	private Long updateUser;

	/**
	 * 更新人名称
	 */
	private String updateUserName;

	/**
	 * 更新人工号
	 */
	private String updateUserCode;

	/**
	 * 备注
	 */
	private String remark;
}
