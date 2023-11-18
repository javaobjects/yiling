package com.yiling.sales.assistant.app.paymentdays.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 临时额度列表 Form
 *
 * @author: lun.yu
 * @date: 2021/9/27
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class QueryShortTimeQuotaAccountForm extends QueryPageListForm {

    /**
     * 审核状态：1-待审核 2-审核通过 3-审核驳回
     */
    @ApiModelProperty(value = "审核状态：1-待审核 2-审核通过 3-审核驳回")
    private Integer status;

}
