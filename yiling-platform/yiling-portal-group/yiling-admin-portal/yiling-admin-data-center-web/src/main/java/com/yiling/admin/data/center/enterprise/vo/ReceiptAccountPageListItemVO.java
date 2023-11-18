package com.yiling.admin.data.center.enterprise.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ReceiptAccountPageListItemVO extends BaseVO {

	/**
	 * 企业名称
	 */
	@ApiModelProperty(value = "企业名称")
	private String name;

	/**
	 * 企业ID
	 */
	@ApiModelProperty(value = "企业ID")
	private Long eid;

	/**
	 * 类型：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
	 */
	@ApiModelProperty(value = "企业类型")
	private Integer type;

	/**
	 * 账户状态 1-待审核 2-审核成功 3-审核失败
	 */
	@ApiModelProperty(value = "账户状态")
	private Integer status;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	/**
	 * 收款账户提交审核时间
	 */
	@ApiModelProperty(value = "收款账户提交审核时间")
	private Date submitTime;

	/**
	 * 审核时间
	 */
	@ApiModelProperty(value = "审核时间")
	private Date auditTime;

	/**
	 * 审核人
	 */
	@ApiModelProperty(value = "审核人",hidden = true)
	@JsonIgnore
	private Long auditUser;

	/**
	 * 审核人
	 */
	@ApiModelProperty(value = "审核人")
	private String auditUserName;

	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间")
	private Date updateTime;

	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人",hidden = true)
	@JsonIgnore
	private Long updateUser;

	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人")
	private String updateUserName;

}
