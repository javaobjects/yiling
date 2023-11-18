package com.yiling.payment.web.receiptdesk.form;

import javax.validation.constraints.NotEmpty;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-12-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GetWechatOpenIdForm extends BaseForm {

	@NotEmpty
	@ApiModelProperty(value = "code")
	private String code;
}
