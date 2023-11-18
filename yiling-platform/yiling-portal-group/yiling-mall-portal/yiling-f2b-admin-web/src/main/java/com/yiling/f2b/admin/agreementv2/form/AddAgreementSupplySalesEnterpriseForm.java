package com.yiling.f2b.admin.agreementv2.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议供销指定商业公司 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-25
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementSupplySalesEnterpriseForm extends BaseForm {

    /**
     * 企业ID
     */
    @NotNull
    @ApiModelProperty(value = "企业ID",required = true)
    private Long eid;

    /**
     * 企业名称
     */
    @NotEmpty
    @ApiModelProperty(value = "企业名称",required = true)
    private String ename;

}
