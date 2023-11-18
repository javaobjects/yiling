package com.yiling.sales.assistant.app.agreement.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2021/9/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("年度协议列表分页VO")
@AllArgsConstructor
@NoArgsConstructor
public class YearAgreementPageVO<T> extends Page<T> {

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
