package com.yiling.hmc.admin.welfare.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: benben.jia
 * @data: 2022/10/08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DrugWelfareVerificationPageForm extends QueryPageListForm {

    @ApiModelProperty("福利计划id")
    private Long drugWelfareId;

    @ApiModelProperty("核销开始时间")
    private Date startTime;

    @ApiModelProperty("核销结束时间")
    private Date endTime;

}
