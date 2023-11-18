package com.yiling.sales.assistant.app.usercustomer.form;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询企业客户 Form
 * 
 * @author lun.yu
 * @date 2022/01/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryEnterpriseForm extends BaseForm {

    /**
     * 社会统一信用代码（精准查询）
     */
    @NotEmpty
    @Length(max = 30)
    @ApiModelProperty(value = "社会统一信用代码（精准查询）",required = true)
    private String licenseNumber;


}
