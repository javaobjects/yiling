package com.yiling.settlement.b2b.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-12-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateSettlementStatusRequest extends BaseRequest {

	/**
	 * 结算单号
	 */
	private String code;

	/**
	 * 结算单类型 1-货款结算单 2-促销结算单 3-预售违约金结算单
	 */
	private Integer type;

}
