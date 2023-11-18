package com.yiling.user.agreement.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-08-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EditApplyDetailRequest extends BaseRequest {

	/**
	 * 企业id
	 */
	private Long eid;

	/**
	 * easCode
	 */
	private String easCode;

	/**
	 * 申请单id
	 */
	private Long applyId;

	/**
	 * 明细id
	 */
	private Long id;

	/**
	 * 返利金额
	 */
	private BigDecimal amount;

	/**
	 * 入账原因
	 */
	private String entryDescribe;


}
