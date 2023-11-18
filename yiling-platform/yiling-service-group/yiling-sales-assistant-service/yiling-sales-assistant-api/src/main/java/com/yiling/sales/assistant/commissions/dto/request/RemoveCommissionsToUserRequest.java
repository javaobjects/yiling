package com.yiling.sales.assistant.commissions.dto.request;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-09-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RemoveCommissionsToUserRequest extends BaseRequest {

    private static final long serialVersionUID = 8897289371136805800L;

	/**
	 * 用户任务id
	 */
	@NotNull
	private Long userTaskId;

	/**
	 * 订单编号
	 */
	@NotNull
	private String orderCode;

}
