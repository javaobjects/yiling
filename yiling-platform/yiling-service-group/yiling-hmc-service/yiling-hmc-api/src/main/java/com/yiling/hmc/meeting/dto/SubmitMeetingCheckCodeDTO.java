package com.yiling.hmc.meeting.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 会议签到DTO
 *
 * @author: fan.shen
 * @data: 2023/04/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SubmitMeetingCheckCodeDTO extends BaseDTO {

    /**
     * 核销结果 0-之前已核销，1-核销成功
     */
    private Integer checkResult;

    /**
     * 签到日期
     */
    private Date signDate;

    /**
     * 核销日期
     */
    private Date checkDate;

}
