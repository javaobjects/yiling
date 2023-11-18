package com.yiling.f2b.admin.agreement.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
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
public class AgreementCloseForm extends BaseForm {

	/**
	 * 协议id
	 */
	@NotNull
	@ApiModelProperty(value = "协议id")
	private Long agreementId;

	/**
	 *操作类型 1-删除 2-停用
	 */
	@NotNull
	@ApiModelProperty(value = "操作类型 1-删除 2-停用")
	private Integer opType;
}
