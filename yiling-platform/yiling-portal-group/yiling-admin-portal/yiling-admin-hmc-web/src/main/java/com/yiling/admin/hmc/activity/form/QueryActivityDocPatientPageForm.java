package com.yiling.admin.hmc.activity.form;

import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 查询患者接口
 * @author: fan.shen
 * @data: 2023-02-02
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class QueryActivityDocPatientPageForm extends QueryPageListForm {

    @NotNull
    @ApiModelProperty("活动id")
    private Integer activityId;

    @ApiModelProperty("患者名称")
    private String patientName;

    @ApiModelProperty("医生id")
    private Integer doctorId;

    @ApiModelProperty("医生名称")
    private String doctorName;

    @ApiModelProperty("凭证状态 全部-0或者null 1-待上传 2-待审核 3-审核通过 4-审核驳回")
    private Integer certificateState;
}
