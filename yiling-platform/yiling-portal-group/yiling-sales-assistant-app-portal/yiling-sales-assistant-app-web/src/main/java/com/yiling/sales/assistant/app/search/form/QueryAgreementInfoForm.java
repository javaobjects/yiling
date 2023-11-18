package com.yiling.sales.assistant.app.search.form;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAgreementInfoForm extends BaseForm {

    /**
     * 协议ID
     */
    @NotNull
    @ApiModelProperty(value = "协议ID",required = true)
    private List<Long> agreementIds;
}
