package com.yiling.b2b.app.promotion.vo;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shixing.sun
 * @date 2022-05-06
 */
@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class SpecialActivityItemInfoVO {

    @ApiModelProperty(value = "专场活动id")
    private String specialActivityId;

    @ApiModelProperty(value = "专场活动名称")
    private String specialActivityName;

    @ApiModelProperty(value = "专场活动开始时间--实际取得营销活动时间")
    private Date startTime;

    @ApiModelProperty(value = "专场活动开始时间--实际取得营销活动时间")
    private Date endTime;

    @ApiModelProperty(value = "营销活动id")
    private Long promotionActivityId;

    @ApiModelProperty(value = "营销活动名称")
    private String promotionActivityName;

    @ApiModelProperty(value = "专场活动图片")
    private String pic;

    @ApiModelProperty(value = "是否预约过")
    private Boolean isAppointment;

    @ApiModelProperty(value = "专场活动企业表ID")
    private Long specialActivityEnterpriseId;
}
