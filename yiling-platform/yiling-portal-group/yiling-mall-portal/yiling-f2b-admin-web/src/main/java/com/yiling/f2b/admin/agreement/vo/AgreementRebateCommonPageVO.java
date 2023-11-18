package com.yiling.f2b.admin.agreement.vo;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2021/7/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("返利-协议分页vo")
public class AgreementRebateCommonPageVO<T> extends Page<T> {

	/**
	 * 协议名称
	 */
	@ApiModelProperty(value = "协议名称")
	private String name;

	/**
	 * 联系人
	 */
	@ApiModelProperty(value = "联系人")
	private String contactor;

	/**
	 * 联系人电话
	 */
	@ApiModelProperty(value = "联系人电话")
	private String contactorPhone;

	/**
	 * 执业许可证号/社会信用统一代码
	 */
	@ApiModelProperty(value = "执业许可证号/社会信用统一代码")
	private String licenseNumber;

	/**
	 * 渠道ID
	 */
	@ApiModelProperty(value = "渠道ID")
	private Long channelId;

	/**
	 * 已兑付金额
	 */
	@ApiModelProperty(value = "已兑付金额")
	private BigDecimal discountAmount;




}
