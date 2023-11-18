package com.yiling.f2b.admin.agreementv2.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议返利范围控销条件 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-04
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementRebateControlForm extends BaseForm {

    /**
     * 控销条件：1-区域 2-客户类型
     */
    @ApiModelProperty("控销条件：1-区域 2-客户类型")
    private Integer controlSaleCondition;

}
