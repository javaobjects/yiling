package com.yiling.sales.assistant.app.commissions.vo;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.ObjectUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.framework.common.base.BaseVO;
import com.yiling.sales.assistant.commissions.enums.CommissionsTypeEnum;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *
 * @author dexi.yao
 * @date 2021-09-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CommissionsPageListItemVO extends BaseVO {

	/**
	 * 用户id
	 */
	@ApiModelProperty(value = "用户id")
	private Long userId;

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

	/**
	 * 佣金类型 1-入账 2-出账
	 */
	@ApiModelProperty(value = "佣金类型 1-入账 2-出账")
	private Integer type;

	/**
	 * 生效时间
	 */
	@ApiModelProperty(value = "入账时间")
	private Date effectTime;

    public BigDecimal getAmount() {
        BigDecimal result=amount.setScale(2, BigDecimal.ROUND_HALF_UP);
        return result;
    }
	
}
