package com.yiling.settlement.b2b.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 补偿更新结算单状态参数
 *
 * @author dexi.yao
 * @date 2021-12-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CompensateSettlementPayStatusRequest extends BaseRequest {

	/**
	 * 要更新的结算单列表
	 */
	List<UpdateSettlementByIdRequest> updateList;

	/**
	 * 打款成功的结算单
	 */
	List<UpdateSettlementStatusRequest> successSettList;

	/**
	 * 打款失败的结算单
	 */
	List<UpdateSettlementStatusRequest> failSettList;
}
