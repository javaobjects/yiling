package com.yiling.sales.assistant.commissions.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.sales.assistant.commissions.enums.CommissionsDetailStatusEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-09-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BatchQueryCommissionsDetailPageListRequest extends QueryPageListRequest {

	/**
	 * 佣金记录id
	 */
	@NotNull
	private List<Long> commissionsIdList;

	/**
	 * 兑付状态 空则不限制
	 */
	private CommissionsDetailStatusEnum statusEnum;
}
