package com.yiling.user.agreement.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-07-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ExportAgreementApplyRequest extends BaseRequest {

	private static final long serialVersionUID = -5136218852596055668L;
	/**
	 * 协议申请id
	 */
	private Long applyId;

}
