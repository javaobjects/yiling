package com.yiling.admin.sales.assistant.commissions.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2021/9/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("用户佣金明细PageVO")
public class CommissionsDetailPageVO<T> extends Page<T> {

	/**
	 * 任务名称
	 */
	@ApiModelProperty(value = "任务名称")
	private String taskName;

	/**
	 * 用户任务id
	 */
	@ApiModelProperty(value = "用户任务id")
	private Long userTaskId;

	/**
	 * 任务id
	 */
	@ApiModelProperty(value = "任务id")
	private Long taskId;

	/**
	 * 任务类型：1-交易额 2-交易量 3-新用户推广 4-促销推广 5-会议推广 6-学术推广 7-新人推广 8-会员推广-购买 10-上传资料
	 */
	@ApiModelProperty(value = "任务类型：1-交易额 2-交易量 3-新用户推广 4-促销推广 5-会议推广 6-学术推广 7-新人推广 8-会员推广-购买 10-上传资料")
	private Integer finishType;

	/**
	 * 开始时间
	 */
	@ApiModelProperty(value = "开始时间", hidden = true)
	@JsonIgnore
	private Date startTime;

	/**
	 * 结束时间
	 */
	@ApiModelProperty(value = "结束时间", hidden = true)
	@JsonIgnore
	private Date endTime;

	/**
	 * 任务起止时间
	 */
	@ApiModelProperty(value = "任务起止时间")
	private String taskTime;

	/**
	 * 任务计算方式
	 */
	@ApiModelProperty(value = "任务计算方式")
	private String ruleValue;

    /**
     * 佣金金额
     */
    @ApiModelProperty(value = "佣金金额")
    private BigDecimal amount;

	/**
	 * 待结算金额
	 */
	@ApiModelProperty(value = "待结算金额")
	private BigDecimal surplusAmount;

	/**
	 * 联系方式
	 */
	@ApiModelProperty(value = "联系方式")
	private String mobile;

	public String getTaskTime() {
		StringBuffer stringBuffer = new StringBuffer();
		if (ObjectUtil.isNotNull(startTime) && ObjectUtil.isNotNull(endTime)) {
			stringBuffer.append(DateUtil.format(startTime, "yyyy-MM-dd"));
			stringBuffer.append("至");
			stringBuffer.append(DateUtil.format(endTime, "yyyy-MM-dd"));
			return stringBuffer.toString();

		} else {
			return "";
		}
	}

}
