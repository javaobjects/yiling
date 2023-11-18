package com.yiling.admin.hmc.insurance.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsurancePageForm extends QueryPageListForm {

    @ApiModelProperty("保险公司id")
    private Long insuranceCompanyId;

    @ApiModelProperty("保险名称")
    private String insuranceName;

    @ApiModelProperty("创建时间查询开始时间")
    private Date startTime;

    @ApiModelProperty("创建时间查询截止时间")
    private Date stopTime;
}
