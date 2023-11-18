package com.yiling.b2b.app.promotion.vo;

import java.util.Date;
import java.util.List;

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
public class SpecialActivityAppointmentVO {
    @ApiModelProperty(value = "已预约")
    private List<SpecialActivityAppointmentItemVO> notStarted;

    @ApiModelProperty(value = "已开始")
    private List<SpecialActivityAppointmentItemVO> started;

    @ApiModelProperty(value = "已结束")
    private List<SpecialActivityAppointmentItemVO> ended;
}
