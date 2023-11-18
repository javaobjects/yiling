package com.yiling.f2b.admin.agreement.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-07-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GetAgreementStatusCountForm extends BaseForm {

	/**
	 * eid
	 */
	@NotNull
	@ApiModelProperty(value = "eid")
	private Long eid;

	/**
	 * 协议类型 1-年度协议 2-补充协议
	 */
	@ApiModelProperty(value = "协议类型 1-年度协议 2-补充协议")
	private Integer agreementCategory;


	/**
	 * 协议model 1-双方协议 2-三方协议
	 */
	@ApiModelProperty(value = "协议model 1-双方协议 2-三方协议")
	private Integer agreementMode;
}
