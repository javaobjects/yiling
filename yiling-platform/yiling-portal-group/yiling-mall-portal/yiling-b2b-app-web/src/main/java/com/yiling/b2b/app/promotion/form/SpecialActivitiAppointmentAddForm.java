package com.yiling.b2b.app.promotion.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/12/13
 */
@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class SpecialActivitiAppointmentAddForm {
    @ApiModelProperty("专场活动企业表ID")
    private Long specialActivityEnterpriseId;

    @ApiModelProperty("专场活动ID")
    private Long specialActivityId;

    @ApiModelProperty("店铺Eid")
    private Long shopEid;

    @ApiModelProperty("预约人id")
    private Long appointmentUserId;

    @ApiModelProperty("预约人所在企业id")
    private Long appointmentUserEid;
}
