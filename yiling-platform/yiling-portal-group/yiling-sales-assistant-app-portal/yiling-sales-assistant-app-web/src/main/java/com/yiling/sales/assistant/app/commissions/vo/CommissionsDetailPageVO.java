package com.yiling.sales.assistant.app.commissions.vo;

import java.math.BigDecimal;

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
 * @date: 2021/9/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("佣金明细VO")
@AllArgsConstructor
@NoArgsConstructor
public class CommissionsDetailPageVO<T> extends Page<T> {

    /**
     * 任务类型：1-交易额 2-交易量 3-新用户推广 4-促销推广 5-会议推广 6-学术推广 7-新人推广 8-会员推广-购买 10-上传资料
     */
    @ApiModelProperty(value = "任务类型：1-交易额 2-交易量 3-新用户推广 4-促销推广 5-会议推广 6-学术推广 7-新人推广 8-会员推广-购买 10-上传资料")
    private Integer finishType;

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
	 * 佣金金额
	 */
	@ApiModelProperty(value = "佣金金额")
	private BigDecimal amount;

	/**
	 * 佣金来源 1-任务收益 2-下线推广
	 */
	@ApiModelProperty(value = "佣金来源 1-任务收益 2-下线推广")
	private Integer sources;

    public BigDecimal getAmount() {
        BigDecimal result=amount.setScale(2, BigDecimal.ROUND_HALF_UP);
        return result;
    }

}
