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
public class SpecialActivityAppointmentItemVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "专场活动ID")
    private Long specialActivityId;

    @ApiModelProperty(value = "营销活动id")
    private Long promotionActivityId;

    @ApiModelProperty(value = "专场活动名称")
    private String specialActivityName;

    @ApiModelProperty(value = "专场活动图片")
    private String pic;

    @ApiModelProperty(value = "营销活动名称")
    private String promotionName;

    @ApiModelProperty(value = "活动开始时间")
    private Date startTime;

    @ApiModelProperty(value = "活动结束时间")
    private Date endTime;

    @ApiModelProperty(value = "企业id")
    private Long eid;

    @ApiModelProperty(value = "活动类型")
    private Integer type;

    @ApiModelProperty(value = "总预约个数")
    private Integer count;
}
