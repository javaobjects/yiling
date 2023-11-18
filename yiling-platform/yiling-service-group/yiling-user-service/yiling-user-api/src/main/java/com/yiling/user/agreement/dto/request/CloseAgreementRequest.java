package com.yiling.user.agreement.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-06-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CloseAgreementRequest extends BaseRequest {

	private static final long serialVersionUID = -5045331341880627054L;
	/**
	 * 协议id
	 */
	private Long agreementId;

	/**
	 *操作类型 1-删除 2-停用
	 */
	private Integer opType;

}
