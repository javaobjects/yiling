package com.yiling.sales.assistant.commissions.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-10-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateCommissionsEffectiveRequest extends BaseRequest {

	/**
	 * 用户id
	 */
	@NotNull
	private Long userId;

	/**
	 * 用户任务id
	 */
	@NotNull
	private Long userTaskId;

    /**
     * 任务类型：任务类型：1-交易额 2-交易量 3-新用户推广 4-促销推广 5-会议推广 6-学术推广 7-新人推广
     */
    private Integer finishType;
}
