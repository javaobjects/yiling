package com.yiling.user.agreement.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
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
public class ExportAgreementBatchApplyRequest extends BaseRequest {

	private static final long serialVersionUID = -5136218852596055668L;

	@ApiModelProperty("企业名称")
	private String name;

	@ApiModelProperty("企业编码")
	private String easCode;

	@ApiModelProperty("当前登录企业id")
	private Long eid;

	@ApiModelProperty("申请单号")
	private String code;

	@ApiModelProperty("申请单状态 0-全部 1-待审核 2-已入账 3-驳回")
	private Integer status;

	@ApiModelProperty("查询类型（默认商务） 1-商务 2-财务---由于b2b_v1.0新增了数据权限此参数作废")
	@Deprecated
	private Integer queryType;


}
