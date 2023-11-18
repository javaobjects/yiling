package com.yiling.f2b.web.enterprise.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 添加收货地址 Form
 * 
 * @author xuan.zhou
 * @date 2021/6/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddDeliveryAddressForm extends BaseForm {

    /**
     * 收货人
     */
    @NotEmpty
    @Length(max = 50)
    @ApiModelProperty(value = "收货人", required = true)
    private String receiver;

    /**
     * 收货人手机号
     */
    @NotEmpty
    @Length(max = 50)
    @ApiModelProperty(value = "收货人手机号", required = true)
    private String mobile;

    /**
     * 详细地址
     */
    @NotEmpty
    @Length(max = 200)
    @ApiModelProperty(value = "详细地址", required = true)
    private String address;

    /**
     * 是否默认：0-否 1-是
     */
    @NotNull
    @ApiModelProperty(value = "是否默认：0-否 1-是", required = true)
    private Integer defaultFlag;

}
