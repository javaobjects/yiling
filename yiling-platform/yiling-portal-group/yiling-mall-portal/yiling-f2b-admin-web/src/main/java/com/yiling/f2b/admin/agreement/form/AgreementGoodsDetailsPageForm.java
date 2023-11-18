package com.yiling.f2b.admin.agreement.form;


import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/6/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementGoodsDetailsPageForm extends QueryPageListForm {

	/**
	 * 协议id
	 */
	@ApiModelProperty(value = "协议主键id")
	@NotNull(message = "协议主键不能为空")
	private Long agreementId;
}
