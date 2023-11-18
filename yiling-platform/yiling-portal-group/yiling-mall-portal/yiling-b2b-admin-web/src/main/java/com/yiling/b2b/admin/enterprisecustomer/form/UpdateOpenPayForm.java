package com.yiling.b2b.admin.enterprisecustomer.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 开通线下支付 Form
 *
 * @author: lun.yu
 * @date: 2022-07-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class UpdateOpenPayForm extends BaseForm {

    /**
     * 客户ID
     */
    @Min(1)
    @NotNull
    @ApiModelProperty(value = "客户ID", required = true)
    private Long customerEid;

    /**
     * 操作类型：1-开通 2-关闭
     */
    @NotNull
    @Range(min = 1, max = 2)
    @ApiModelProperty(value = "操作类型：1-开通 2-关闭", required = true)
    private Integer opType;

}
