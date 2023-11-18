package com.yiling.sales.assistant.commissions.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.sales.assistant.commissions.enums.CommissionsStatusEnum;
import com.yiling.sales.assistant.commissions.enums.CommissionsTypeEnum;
import com.yiling.sales.assistant.commissions.enums.EffectStatusEnum;

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
public class QueryCommissionsPageListRequest extends QueryPageListRequest {

	/**
	 * 用户id
	 */
	@NotNull
	private List<Long> userIdList;

	/**
	 * 收支类型，空为全部
	 */
	private CommissionsTypeEnum typeEnum;

	/**
	 * 佣金是否有效，空为全部
	 */
	private EffectStatusEnum effectStatusEnum;

	/**
	 * 结算状态，空为全部
	 */
	private CommissionsStatusEnum statusEnum;
}
