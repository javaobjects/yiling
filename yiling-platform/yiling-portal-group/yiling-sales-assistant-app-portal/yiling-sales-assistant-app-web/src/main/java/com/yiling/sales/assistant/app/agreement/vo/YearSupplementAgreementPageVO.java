package com.yiling.sales.assistant.app.agreement.vo;

import java.util.Date;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2021/9/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("年度协议下的补充协议分页VO")
public class YearSupplementAgreementPageVO<T> extends Page<T> {

	@ApiModelProperty(value = "协议id")
	private Long id;

	/**
	 * 协议名称
	 */
	@ApiModelProperty(value = "协议名称")
	private String name;

	/**
	 * 开始时间
	 */
	@ApiModelProperty(value = "开始时间")
	private Date startTime;

	/**
	 * 结束时间
	 */
	@ApiModelProperty(value = "结束时间")
	private Date endTime;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	/**
	 * 进行中
	 */
	@ApiModelProperty(value = "进行中")
	private Integer start;

	/**
	 * 未开始
	 */
	@ApiModelProperty(value = "未开始")
	private Integer unStart;

	/**
	 * 已停用
	 */
	@ApiModelProperty(value = "已停用")
	private Integer stop;

	/**
	 * 已过期
	 */
	@ApiModelProperty(value = "已过期")
	private Integer expire;

}
