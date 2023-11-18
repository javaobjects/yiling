package com.yiling.f2b.admin.agreement.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议兑付日志VO
 * </p>
 *
 * @author dexi.yao
 * @date 2021-07-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementRebateLogPageListItemVo extends BaseVO {


	/**
	 * 客户对应的账号
	 */
	@ApiModelProperty(value = "账号")
	private String account;

	/**
	 * 日志记录名称
	 */
	@ApiModelProperty(value = "日志记录名称")
	private String logName;

	/**
	 * 协议id
	 */
	@ApiModelProperty(value = "协议id")
	private Long agreementId;


	/**
	 * 兑付类型 1协议兑付2手动兑付
	 */
	@ApiModelProperty(value = "兑付类型 1协议兑付2手动兑付")
	private Integer cashType;

	/**
	 * 兑付金额
	 */
	@ApiModelProperty(value = "兑付金额")
	private BigDecimal discountAmount;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人", hidden = true)
	@JsonIgnore
	private Long createUser;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private Long createUserName;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
}
