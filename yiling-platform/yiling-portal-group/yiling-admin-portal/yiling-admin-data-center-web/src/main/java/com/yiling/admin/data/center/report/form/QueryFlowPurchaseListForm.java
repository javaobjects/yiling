package com.yiling.admin.data.center.report.form;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fucheng.bai
 * @date 2022/7/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryFlowPurchaseListForm extends BaseForm {

    // 采购年月，格式为yyyy-MM
    @ApiModelProperty(value = "采购年月，格式为yyyy-MM，可为空")
    private String time;

    // 采购商业渠道类型
    @ApiModelProperty(value = "采购商业渠道类型id，可为空")
    private Integer purchaseChannelId;

    // 供应商渠道类型
    @ApiModelProperty(value = "供应商渠道类型id，可为空")
    private Integer supplierChannelId;

    // 采购商业Id列表
    @ApiModelProperty(value = "采购商业Id列表，可为空")
    private List<Long> purchaseEnterpriseIds;

    // 供应商Id列表
    @ApiModelProperty(value = "供应商Id列表，可为空")
    private List<Long> supplierEnterpriseIds;
}
