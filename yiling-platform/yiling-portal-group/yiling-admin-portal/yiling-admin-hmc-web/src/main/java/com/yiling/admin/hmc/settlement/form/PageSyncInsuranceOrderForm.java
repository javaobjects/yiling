package com.yiling.admin.hmc.settlement.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/7/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PageSyncInsuranceOrderForm extends QueryPageListForm {

    @ApiModelProperty("保险提供服务商id")
    private Long insuranceCompanyId;

    @ApiModelProperty("开方状态 0-全部 1-已开，2-未开")
    private Integer prescriptionStatus;
}
