package com.yiling.sales.assistant.app.usercustomer.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询用户客户资质信息 Form
 * 
 * @author lun.yu
 * @date 2022/01/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCustomerCertificateForm extends BaseForm {

    /**
     * 客户企业ID
     */
    @ApiModelProperty(value = "客户企业ID（审核失败时，客户基本信息点下一步使用此接口需传入此字段；无入驻客户信息，客户基本信息点下一步，无须传入此字段）")
    private Long customerEid;

    /**
     * 企业类型
     */
    @NotNull
    @Range(min = 1,max = 8)
    @ApiModelProperty(value = "企业类型",required = true)
    private Integer type;


}
