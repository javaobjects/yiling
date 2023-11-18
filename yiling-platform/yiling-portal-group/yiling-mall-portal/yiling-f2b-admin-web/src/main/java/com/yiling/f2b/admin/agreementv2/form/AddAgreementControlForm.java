package com.yiling.f2b.admin.agreementv2.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议控销条件 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementControlForm extends BaseForm {

    /**
     * 控销条件：1-区域 2-客户类型
     */
    @ApiModelProperty("控销条件：1-区域 2-客户类型")
    private Integer controlSaleCondition;

}
