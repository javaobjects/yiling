package com.yiling.f2b.admin.agreement.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveAgreementMultipleConditionForm extends BaseForm {

    /**
     * 条件类型
     */
    @ApiModelProperty(value ="多选类型 （回款方式:back_amount_type,支付方式:pay_type,返利形式:restitution_type）")
    private String conditionType;

    /**
     * 条件值
     */
    @ApiModelProperty(value ="多选类型条件值")
    private Integer conditionValue;
}
