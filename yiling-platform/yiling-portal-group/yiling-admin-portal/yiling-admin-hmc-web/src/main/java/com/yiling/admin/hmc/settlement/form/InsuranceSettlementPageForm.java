package com.yiling.admin.hmc.settlement.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/3/31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsuranceSettlementPageForm extends QueryPageListForm {

    @ApiModelProperty("保险提供服务商名称")
    private String insuranceCompanyName;

    @ApiModelProperty("结账日期段-起始时间")
    private Date startTime;

    @ApiModelProperty("结账日期段-截止时间")
    private Date stopTime;
}
