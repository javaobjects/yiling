package com.yiling.user.agreement.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议返利申请使用明细表
 * </p>
 *
 * @author dexi.yao
 * @date 2021-08-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("agreement_use_detail")
public class UseDetailDO extends BaseDO {

    private static final long serialVersionUID = 1L;

	/**
	 * 申请表id
	 */
	private Long useId;

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
	 * 是否删除：0-否 1-是
	 */
	@TableLogic
	private Integer delFlag;

	/**
	 * 创建人
	 */
	@TableField(fill = FieldFill.INSERT)
	private Long createUser;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;

	/**
	 * 修改人
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Long updateUser;

	/**
	 * 修改时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;

	/**
	 * 备注
	 */
	private String remark;


}
