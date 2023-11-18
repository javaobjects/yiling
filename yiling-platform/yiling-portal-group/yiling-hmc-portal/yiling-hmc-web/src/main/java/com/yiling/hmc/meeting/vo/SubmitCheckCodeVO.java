package com.yiling.hmc.meeting.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 会议核销VO
 * @author: fan.shen
 * @data: 2023-03-14
 */
@Data
@ApiModel("SubmitCheckCodeVO")
public class SubmitCheckCodeVO {

    @ApiModelProperty("核销结果 0-之前已核销，1-核销成功")
    private Integer checkResult;

    @ApiModelProperty("签到日期")
    private Date signDate;

    @ApiModelProperty("核销日期")
    private Date checkDate;

}
