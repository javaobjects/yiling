package com.yiling.b2b.admin.paymentdays.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询账期账户分页列表 Form
 *
 * @author lun.yu
 * @date 2021/10/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryPaymentDaysForm extends QueryPageListForm {

    /**
     * 采购商名称
     */
    @ApiModelProperty(value = "采购商名称")
    private String customerName;

    /**
     * 状态：0-全部 1-启用 2-停用
     */
    @ApiModelProperty(value = "状态：0-全部 1-启用 2-停用")
    private Integer status;

}
