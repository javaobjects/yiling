package com.yiling.user.agreement.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-07-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class QueryRebateOrderDetailPageListRequest extends QueryPageListRequest {

	/**
	 * 采购方eid =null时不限制
	 */
	private Long secondEid;

	/**
	 * eas账号 =null时不限制
	 */
	private String easCode;

	/**
	 * 订单id
	 */
	@NotNull
	private List<Long> orderIdList;

	/**
	 * 退货单Id
	 */
	@NotNull
	private List<Long> returnIdList;
}
