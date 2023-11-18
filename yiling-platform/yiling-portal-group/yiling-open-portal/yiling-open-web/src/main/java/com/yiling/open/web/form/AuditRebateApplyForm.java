package com.yiling.open.web.form;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2021/7/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AuditRebateApplyForm extends BaseForm {


	/**
	 * 申请单id
	 */
	@ApiModelProperty(value = "申请单id")
	@NotNull
	private String applicantId;

	/**
	 * 申请单状态1-草稿 2-为提交 3-审核成功 4-驳回 -5撤回
	 */
	@ApiModelProperty(value = "审核状态 3-审核成功 4-驳回")
	@Max(value = 4)
	@Min(value = 3)
	@NotNull
	private Integer status;

	/**
	 * 审核意见
	 */
	@ApiModelProperty(value = "审核意见")
	private String auditRemark;

	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "审核时间")
	@NotNull
	private Date updateTime;

	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "审核人Id")
	private Long updateUser;

	/**
	 * 更新人名称
	 */
	@ApiModelProperty(value = "审核人名称")
	@NotNull
	private String updateUserName;

	/**
	 * 更新人工号
	 */
	@ApiModelProperty(value = "审核人工号")
	@NotNull
	private String updateUserCode;

}
