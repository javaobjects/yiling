package com.yiling.f2b.admin.agreement.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2021/7/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAgreementAccountPageForm extends BaseForm {

    /**
     * 企业id
     */
	@NotNull
	@ApiModelProperty(value = "企业id")
    private Long eid;

    /**
     * 账号信息
     */
	@NotEmpty
	@ApiModelProperty(value = "账号信息")
    private String easAccount;
}
